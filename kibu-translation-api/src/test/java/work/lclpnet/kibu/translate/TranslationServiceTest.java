package work.lclpnet.kibu.translate;

import org.junit.jupiter.api.Test;
import work.lclpnet.translations.DefaultLanguageTranslator;
import work.lclpnet.translations.loader.translation.TranslationLoader;
import work.lclpnet.translations.model.StaticLanguage;
import work.lclpnet.translations.model.StaticLanguageCollection;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TranslationServiceTest {

    @Test
    void translateText_argTranslatedText_transformed() {
        TranslationLoader loader = () -> {
            var language = new StaticLanguage(Map.of("hello", "Hello %s", "world", "World"));
            var collection = new StaticLanguageCollection(Map.of("en_us", language));
            return CompletableFuture.completedFuture(collection);
        };

        var translator = new DefaultLanguageTranslator(loader);
        translator.reload().join();

        var translationService = new TranslationService(translator, player -> Optional.empty());

        var nestedText = translationService.translateText("world");

        var text = translationService.translateText("en_us", "hello", nestedText);

        assertEquals("Hello World", text.getString());
    }
}