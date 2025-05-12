package work.lclpnet.kibu.assets;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * A manager for retrieving Minecraft client assets.
 * More information on assets can be found on the <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Game_files">Minecraft Wiki</a>
 */
public class AssetManager {

    public static final URI VERSIONS_URI = URI.create("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json");
    public static final URI RESOURCES_URI = URI.create("https://resources.download.minecraft.net");
    public static final String DOWNLOAD_CLIENT = "client";

    private static volatile AssetManager sharedInstance = null;

    private final Path assetsRoot;
    private final String minecraftVersion;
    private final Logger logger;
    private final Object indexLock = new Object();
    private final Map<String, Download> downloads = new ConcurrentHashMap<>();

    private volatile JSONObject index = null;
    private volatile boolean indexFetched = false;
    private volatile JSONObject versionData = null;
    private volatile boolean versionDataFetched = false;

    public AssetManager(Path assetsRoot, String minecraftVersion, Logger logger) {
        this.assetsRoot = assetsRoot;
        this.minecraftVersion = minecraftVersion;
        this.logger = logger;
    }

    @Nullable
    public Path getAsset(String asset) {
        JSONObject index = getIndex();

        if (index == null) {
            return null;
        }

        JSONObject entry = index.optJSONObject(asset);

        if (entry == null) {
            return null;
        }

        String hash = entry.optString("hash");

        if (hash == null) {
            return null;
        }

        return getAssetByHash(hash);
    }

    /**
     * Gets a local path for a given download of the configured Minecraft version.
     * If the download is not yet present on the local machine, it is downloaded.
     * <br>
     * Example: Downloading the Minecraft client
     * <code>@Nullable Path clientPath = assetManager.getDownload(AssetManager.DOWNLOAD_CLIENT)</code>
     *
     * @param download The download id string. This can be any key that exists on the downloads config of the configured version.
     * @return The path of the downloaded file, or null if it doesn't exist or if there was an error.
     */
    @Nullable
    public Path getDownload(String download) {
        JSONObject data = getVersionData();

        if (data == null) {
            return null;
        }

        JSONObject downloads = data.optJSONObject("downloads");

        if (downloads == null) {
            logger.error("Downloads not defined in version data");
            return null;
        }

        JSONObject info = downloads.optJSONObject(download);

        if (info == null) {
            logger.error("Unknown download {}", download);
            return null;
        }

        String urlStr = info.optString("url");

        if (urlStr == null) {
            logger.error("Download url is not defined for download {}", download);
            return null;
        }

        URL url = getUrl(urlStr);

        if (url == null) {
            return null;
        }

        String fileName;

        try {
            fileName = Path.of(url.getPath()).getFileName().toString();
        } catch (InvalidPathException e) {
            fileName = download;
        }

        Path path = assetsRoot.resolve("downloads").resolve(minecraftVersion).resolve(fileName);
        String hash = minecraftVersion + "/" + download;

        return getLocalOrDownload(hash, path, () -> saveDownload(url, path));
    }

    @VisibleForTesting
    @Nullable
    JSONObject getIndex() {
        // double-checked locking to ensure index is only downloaded once
        if (indexFetched) {
            return index;
        }

        // if another thread is currently downloading, this thread needs to wait until completed
        synchronized (indexLock) {
            if (indexFetched) {
                return index;
            }

            try {
                // will block current thread
                index = readIndex();
            } finally {
                indexFetched = true;
            }
        }

        return index;
    }

    @VisibleForTesting
    @Nullable
    JSONObject readIndex() {
        var data = fetchIndexData();

        if (data == null) {
            return null;
        }

        Path path = getIndexPath(data);

        if (path == null) {
            return null;
        }

        JSONObject index = readJson(path);

        if (index == null) {
            return null;
        }

        return index.optJSONObject("objects");
    }

    @Nullable
    private IndexData fetchIndexData() {
        JSONObject data = getVersionData();

        if (data == null) {
            return null;
        }

        JSONObject assetIndex = data.optJSONObject("assetIndex");

        if (assetIndex == null) {
            logger.error("Failed to find assetIndex entry in version data");
            return null;
        }

        String assetsId = assetIndex.optString("id");

        if (assetsId == null) {
            logger.error("Failed to find assets id in assetIndex");
            return null;
        }

        String assetsUrl = assetIndex.optString("url");

        if (assetsUrl == null) {
            logger.error("Failed to find assets url in assetIndex");
            return null;
        }

        URL url = getUrl(assetsUrl);

        if (url == null) {
            return null;
        }

        return new IndexData(assetsId, url);
    }

    @VisibleForTesting
    @Nullable
    JSONObject getVersionData() {
        if (versionDataFetched) {
            return versionData;
        }

        synchronized (indexLock) {
            if (versionDataFetched) {
                return versionData;
            }

            try {
                versionData = fetchVersionData();
            } finally {
                versionDataFetched = true;
            }
        }

        return versionData;
    }

    @VisibleForTesting
    @Nullable
    JSONObject fetchVersionData() {
        // this method is synchronized via the indexLock monitor
        Path path = assetsRoot.resolve("versions").resolve(minecraftVersion + ".json");

        if (Files.exists(path)) {
            JSONObject localData = readJson(path);

            if (localData != null) {
                return localData;
            }
        }

        URL versionUrl = getCurrentVersionUrl();

        if (versionUrl == null) {
            return null;
        }

        if (!saveFile(versionUrl, path)) {
            return null;
        }

        return readJson(path);
    }

    @Nullable
    private URL getCurrentVersionUrl() {
        JSONObject manifest = getManifest();

        if (manifest == null) {
            return null;
        }

        JSONArray versions = manifest.optJSONArray("versions");

        if (versions == null) {
            logger.error("Could not find versions array in manifest");
            return null;
        }

        URL versionUrl = null;

        for (Object item : versions) {
            if (!(item instanceof JSONObject version)) continue;

            String id = version.optString("id");

            if (id == null || !id.equals(minecraftVersion)) continue;

            String url = version.optString("url");

            if (url == null) {
                logger.error("Version manifest entry {} doesn't have an url field", id);
                break;
            }

            versionUrl = getUrl(url);
            break;
        }

        if (versionUrl == null) {
            logger.error("Could not find download url for current version {}", minecraftVersion);
            return null;
        }

        if (!"https".equals(versionUrl.getProtocol())) {
            logger.error("Expected version url to use the https protocol, but got {}", versionUrl);
            return null;
        }

        return versionUrl;
    }

    private @Nullable JSONObject getManifest() {
        URL url;

        try {
            url = VERSIONS_URI.toURL();
        } catch (MalformedURLException e) {
            logger.error("Failed to create versions url", e);
            return null;
        }

        return readJson(url);
    }

    @Nullable
    private JSONObject readJson(Path path) {
        try {
            String content = Files.readString(path, StandardCharsets.UTF_8);
            return new JSONObject(content);
        } catch (IOException e) {
            logger.error("Failed to read {}", path, e);
            return null;
        } catch (JSONException e) {
            logger.error("Failed to read JSON from {}", path, e);
            return null;
        }
    }

    @Nullable
    private Path getIndexPath(IndexData data) {
        Path path = assetsRoot.resolve("indexes").resolve(data.id() + ".json");

        if (Files.exists(path)) {
            return path;
        }

        return downloadIndex(data, path);
    }

    @Nullable
    private Path downloadIndex(IndexData data, Path path) {
        URL url = data.url();

        if (!saveFile(url, path)) {
            return null;
        }

        return path;
    }

    @VisibleForTesting
    @Nullable
    Path getAssetByHash(String hash) {
        if (hash.length() != 40) {
            logger.error("Invalid asset hash {}. Expected hash of length 40", hash);
            return null;
        }

        String prefix = hash.substring(0, 2);

        Path path = assetsRoot.resolve("objects").resolve(prefix).resolve(hash);

        return getLocalOrDownload(hash, path, () -> downloadAsset(hash, prefix, path));
    }

    @VisibleForTesting
    @Nullable
    Path getLocalOrDownload(String hash, Path path, Supplier<Path> downloader) {
        // if asset is not still downloading and if it exists, return directly
        if (!downloads.containsKey(hash) && Files.exists(path)) {
            return path;
        }

        // get download, if it was still started by another thread
        var download = downloads.computeIfAbsent(hash, h -> new Download());

        synchronized (download) {
            if (!downloads.containsKey(hash)) {
                // already downloaded by other thread
                return download.resultPath;
            }

            try {
                download.resultPath = downloader.get();
            } finally {
                downloads.remove(hash);
            }

            return download.resultPath;
        }
    }

    @VisibleForTesting
    @Nullable
    Path downloadAsset(String hash, String prefix, Path path) {
        URL url;

        try {
            url = RESOURCES_URI.resolve("/%s/%s".formatted(prefix, hash)).toURL();
        } catch (IllegalArgumentException | MalformedURLException e) {
            logger.error("Failed to construct download URL for hash {}", hash, e);
            return null;
        }

        return saveDownload(url, path);
    }

    @Nullable
    private Path saveDownload(URL url, Path path) {
        if (!saveFile(url, path)) {
            return null;
        }

        return path;
    }

    private URL getUrl(String string) {
        try {
            return URI.create(string).toURL();
        } catch (MalformedURLException e) {
            logger.error("Failed to create URL {}", string, e);
            return null;
        }
    }

    @VisibleForTesting
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean saveFile(URL url, Path path) {
        var conn = connectOk(url);

        if (conn == null) {
            return false;
        }

        Path dir = path.getParent();

        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            logger.error("Failed to create directory {}", dir, e);
            return false;
        }

        try (var in = conn.getInputStream();
             var out = Files.newOutputStream(path)) {

            in.transferTo(out);
        } catch (IOException e) {
            logger.error("Failed to download {}", conn.getURL(), e);
            return false;
        }

        return true;
    }

    @VisibleForTesting
    @Nullable
    JSONObject readJson(URL url) {
        var conn = connectOk(url);

        if (conn == null) {
            return null;
        }

        try (var in = conn.getInputStream()) {
            byte[] bytes = in.readAllBytes();
            String json = new String(bytes, StandardCharsets.UTF_8);

            return new JSONObject(json);
        } catch (IOException e) {
            logger.error("Failed to content from {}", url, e);
            return null;
        } catch (JSONException e) {
            logger.error("Failed to read JSON from {}", url, e);
            return null;
        }
    }

    @Nullable
    private HttpsURLConnection connectOk(URL url) {
        try {
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            int status = conn.getResponseCode();

            if (status != 200) {
                logger.error("Failed to connect to {}; got HTTP status {} but expected 200", url, status);
                return null;
            }

            return conn;
        } catch (IOException e) {
            logger.error("Failed to connect to {}", url, e);
            return null;
        }
    }

    /**
     * Get a JVM-Shared AssetManager instance, so that results can be cached.
     * This method assumes that only one minecraft version is present in the current JVM.
     * @return A shared instance of an AssetManager.
     */
    public static AssetManager getShared(String minecraftVersion) {
        if (sharedInstance != null) {
            return sharedInstance;
        }

        synchronized (AssetManager.class) {
            if (sharedInstance == null) {
                Path assetsRoot = OsUtil.getCacheDir().resolve("kibu").resolve("mc_assets");
                Logger logger = LoggerFactory.getLogger(AssetManager.class);

                sharedInstance = new AssetManager(assetsRoot, minecraftVersion, logger);
            }
        }

        return sharedInstance;
    }

    private record IndexData(String id, URL url) {}

    private static class Download {
        @Nullable Path resultPath = null;
    }
}
