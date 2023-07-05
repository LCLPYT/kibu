package work.lclpnet.kibu.translate;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import work.lclpnet.kibu.access.PlayerLanguage;
import work.lclpnet.kibu.translate.bossbar.BossBarProvider;
import work.lclpnet.kibu.translate.bossbar.TranslatedBossBar;
import work.lclpnet.kibu.translate.pref.LanguagePreferenceProvider;
import work.lclpnet.kibu.translate.text.RootText;
import work.lclpnet.kibu.translate.text.TextFormatter;
import work.lclpnet.kibu.translate.text.TranslatedText;
import work.lclpnet.kibu.translate.util.Partial;
import work.lclpnet.translations.Translator;

import javax.annotation.Nonnull;

public class TranslationService {

    private final Translator translator;
    private final TextFormatter textFormatter = new TextFormatter();
    private final LanguagePreferenceProvider languagePreferenceProvider;

    public TranslationService(Translator translator, LanguagePreferenceProvider languagePreferenceProvider) {
        this.translator = translator;
        this.languagePreferenceProvider = languagePreferenceProvider;
    }

    public Translator getTranslator() {
        return translator;
    }

    @Nonnull
    public String getLanguage(ServerPlayerEntity player) {
        return languagePreferenceProvider.getLanguagePreference(player)
                .orElseGet(() -> PlayerLanguage.getLanguage(player));
    }

    public String translate(ServerPlayerEntity player, String key) {
        String language = getLanguage(player);
        return translator.translate(language, key);
    }

    public String translate(ServerPlayerEntity player, String key, Object... args) {
        String language = getLanguage(player);
        return translator.translate(language, key, args);
    }

    public RootText translateText(ServerPlayerEntity player, String key, Object... args) {
        return translateText(getLanguage(player), key, args);
    }

    public RootText translateText(String language, String key, Object... args) {
        String raw = translator.translate(language, key);  // do not replace format specifiers just yet

        return textFormatter.formatText(raw, args);
    }

    public TranslatedText translateText(String key, Object... args) {
        return TranslatedText.create(player -> translateText(player, key, args));
    }

    public Partial<TranslatedBossBar, BossBarProvider> translateBossBar(Identifier id, String key, Object... args) {
        return handler -> new TranslatedBossBar(handler, id, this, key, args);
    }
}
