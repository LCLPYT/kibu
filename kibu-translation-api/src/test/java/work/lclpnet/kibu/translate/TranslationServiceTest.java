package work.lclpnet.kibu.translate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import work.lclpnet.translations.DefaultLanguageTranslator;
import work.lclpnet.translations.loader.translation.TranslationLoader;
import work.lclpnet.translations.model.StaticLanguage;
import work.lclpnet.translations.model.StaticLanguageCollection;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("localeArgs")
    void getLocale_fromPlayer_mappedCorrectly(String language, Locale expected) {
        var translator = new DefaultLanguageTranslator(() -> CompletableFuture.completedFuture(new StaticLanguageCollection(Map.of())));
        translator.reload().join();

        var translationService = new TranslationService(translator, player -> Optional.of(language));

        Locale locale = translationService.getLocale(null);
        assertEquals(expected, locale);
    }

    private static Stream<Arguments> localeArgs() {
        return Stream.of(
                Arguments.of("en_us", Locale.ENGLISH),
                Arguments.of("de_de", Locale.GERMAN),
                Arguments.of("de_at", Locale.GERMAN),
                Arguments.of("ja_jp", Locale.JAPANESE),
                Arguments.of("fr_fr", Locale.FRENCH),
                Arguments.of("it_it", Locale.ITALIAN),
                Arguments.of("ko_kr", Locale.KOREAN),
                Arguments.of("zh_cn", Locale.CHINESE),
                Arguments.of("asdadslkdsaklsad", Locale.ENGLISH)  // default is english
        );
    }
}