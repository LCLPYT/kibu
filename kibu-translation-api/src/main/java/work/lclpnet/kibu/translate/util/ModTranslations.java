package work.lclpnet.kibu.translate.util;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import work.lclpnet.kibu.translate.Translations;
import work.lclpnet.translations.DefaultLanguageTranslator;
import work.lclpnet.translations.loader.TranslationLoader;
import work.lclpnet.translations.loader.UrlLanguageLoader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * A utility class providing functions for loading mod translations.
 */
public class ModTranslations {

    private ModTranslations() {}

    public static TranslationLoader assetTranslationLoader(String modId, Logger logger) {
        var locations = FabricLoader.getInstance()
                .getModContainer(modId)
                .orElseThrow(() -> new NoSuchElementException("Failed to find mod container"))
                .getRootPaths()
                .stream()
                .map(path -> {
                    try {
                        return path.toUri().toURL();
                    } catch (MalformedURLException e) {
                        logger.error("Failed to convert path {} to url", path, e);
                        return null;
                    }
                }).filter(Objects::nonNull)
                .toArray(URL[]::new);

        return new UrlLanguageLoader(locations, List.of("assets/%s/lang/".formatted(modId)), logger);
    }

    /**
     * Loads translation files from the <code>assets/modid/lang/</code> directory of a mod.
     * This method will load the standard translations files that would otherwise be available on the client in the I18n class.
     * However, unlike the Minecraft translations, not every translation source from each mod is merged.
     * The resulting {@link Translations} only contains the translations of the specified mod.
     * @param modId The mod id.
     * @param logger A logger.
     * @return A {@link Result} containing the resulting {@link Translations} and a void future as callback for when the translations are loaded.
     */
    public static Result fromAssets(String modId, Logger logger) {
        var loader = assetTranslationLoader(modId, logger);
        var translator = new DefaultLanguageTranslator(loader);
        var translations = new Translations(translator);

        var whenLoaded = translator.reload();

        return new Result(translations, whenLoaded);
    }

    public record Result(Translations translations, CompletableFuture<Void> whenLoaded) {}
}
