package work.lclpnet.kibu.assets;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AssetManagerTest {

    static final Logger logger = LoggerFactory.getLogger(AssetManagerTest.class);
    AssetManager assetManager;
    boolean simulateDelay = false;

    @BeforeEach
    void setUp() throws IOException {
        Path assetsRoot = Files.createTempDirectory("mc_assets");
        String version = "1.21";  // real program should use MinecraftVersion.CURRENT.getName();
        AssetManager assetManager = spy(new AssetManager(assetsRoot, version, logger));

        // stub methods that use network requests
        // :: readJson(URL)
        doAnswer(invocation -> {
            String url = invocation.getArgument(0, URL.class).toString();

            if (simulateDelay) {
                Thread.sleep(30);
            }

            if (url.equals("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json")) {
                return new JSONObject("""
                        {"versions": [{"id": "1.21", "url": "https://piston-meta.mojang.com/v1/packages/da2bc4e9f46906f7199a8ac661e08d64c6bc28f4/1.21.json"}]}
                        """);
            }

            return null;
        }).when(assetManager).readJson(any());

        // :: saveFile(URL, Path)
        doAnswer(invocation -> {
            String url = invocation.getArgument(0, URL.class).toString();

            if (simulateDelay) {
                Thread.sleep(30);
            }

            String content = switch (url) {
                case "https://piston-meta.mojang.com/v1/packages/483db51cbd4335190b40f225213b7b03a1075a80/17.json" -> """
                        {"objects": {
                          "pack.mcmeta": {"hash": "bde49142dab36670c2e2838128aa53208cd40f95"}
                        }}
                        """;
                case "https://resources.download.minecraft.net/bd/bde49142dab36670c2e2838128aa53208cd40f95" -> """
                        {"language": {"de_de": {}}}
                        """;
                case "https://piston-meta.mojang.com/v1/packages/da2bc4e9f46906f7199a8ac661e08d64c6bc28f4/1.21.json" -> """
                        {
                          "assetIndex":{"id": "17", "url": "https://piston-meta.mojang.com/v1/packages/483db51cbd4335190b40f225213b7b03a1075a80/17.json"},
                          "downloads":{"client":{"url":"https://piston-data.mojang.com/v1/objects/0e9a07b9bb3390602f977073aa12884a4ce12431/client.jar"}}
                          }
                        """;
                case "https://piston-data.mojang.com/v1/objects/0e9a07b9bb3390602f977073aa12884a4ce12431/client.jar" -> "stub";
                default -> null;
            };

            if (content == null) return null;

            Path path = invocation.getArgument(1, Path.class);

            Files.createDirectories(path.getParent());
            Files.writeString(path, content, StandardCharsets.UTF_8);

            return true;
        }).when(assetManager).saveFile(any(), any());

        this.assetManager = assetManager;
    }

    @Test
    void getAsset() throws IOException {
        Path asset = assetManager.getAsset("pack.mcmeta");

        assertNotNull(asset);

        String content = Files.readString(asset, StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(content);

        assertTrue(json.similar(new JSONObject("{\"language\": {\"de_de\": {}}}")));
    }

    @Test
    void getAsset_concurrent_downloaded() throws InterruptedException {
        simulateDelay = true;

        int parallelCalls = 3;
        List<Thread> threads = new ArrayList<>(parallelCalls);
        AtomicReference<Path> path = new AtomicReference<>(null);
        Object lock = new Object();

        for (int i = 0; i < parallelCalls; i++) {
            threads.add(Thread.startVirtualThread(() -> {
                Path asset = assetManager.getAsset("pack.mcmeta");

                synchronized (lock) {
                    Path val = path.get();

                    if (val == null) {
                        assertNotNull(asset);
                        path.set(asset);
                    } else {
                        assertEquals(val, asset);
                    }
                }
            }));
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // readIndex should only have been called once, because the other threads should wait for the first call to complete
        verify(assetManager, times(3)).getIndex();
        verify(assetManager, times(1)).readIndex();

        // downloadAsset should only have been called once, because the other threads should wait for the first call to complete
        verify(assetManager, times(3)).getAssetByHash("bde49142dab36670c2e2838128aa53208cd40f95");
        verify(assetManager, times(1)).downloadAsset(eq("bde49142dab36670c2e2838128aa53208cd40f95"), eq("bd"), any());
    }

    @Test
    void getDownload() throws IOException {
        Path client = assetManager.getDownload(AssetManager.DOWNLOAD_CLIENT);

        assertNotNull(client);

        String content = Files.readString(client, StandardCharsets.UTF_8);

        assertEquals("stub", content);
    }

    @Test
    void getDownload_concurrent_downloaded() throws InterruptedException, MalformedURLException {
        simulateDelay = true;

        int parallelCalls = 3;
        List<Thread> threads = new ArrayList<>(parallelCalls);
        AtomicReference<Path> path = new AtomicReference<>(null);
        Object lock = new Object();

        for (int i = 0; i < parallelCalls; i++) {
            threads.add(Thread.startVirtualThread(() -> {
                Path client = assetManager.getDownload(AssetManager.DOWNLOAD_CLIENT);

                synchronized (lock) {
                    Path val = path.get();

                    if (val == null) {
                        assertNotNull(client);
                        path.set(client);
                    } else {
                        assertEquals(val, client);
                    }
                }
            }));
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // readIndex should only have been called once, because the other threads should wait for the first call to complete
        verify(assetManager, times(3)).getVersionData();
        verify(assetManager, times(1)).fetchVersionData();

        // saveFile should only have been called once, because the other threads should wait for the first call to complete
        verify(assetManager, times(3)).getLocalOrDownload(eq("1.21/client"), any(), any());

        URL url = URI.create("https://piston-data.mojang.com/v1/objects/0e9a07b9bb3390602f977073aa12884a4ce12431/client.jar").toURL();
        verify(assetManager, times(1)).saveFile(eq(url), any());
    }
}