package work.lclpnet.kibu.translate;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import work.lclpnet.kibu.access.PlayerLanguage;
import work.lclpnet.kibu.translate.bossbar.BossBarProvider;
import work.lclpnet.kibu.translate.bossbar.TranslatedBossBar;
import work.lclpnet.kibu.translate.hook.LanguageChangedCallback;
import work.lclpnet.kibu.translate.pref.LanguagePreferenceProvider;
import work.lclpnet.kibu.translate.text.RootText;
import work.lclpnet.kibu.translate.text.TextFormatter;
import work.lclpnet.kibu.translate.text.TextTranslatable;
import work.lclpnet.kibu.translate.text.TranslatedText;
import work.lclpnet.kibu.translate.util.Partial;
import work.lclpnet.kibu.translate.util.WeakList;
import work.lclpnet.translations.Translator;

import javax.annotation.Nonnull;

public class TranslationService {

    private static final WeakList<TranslationService> services = new WeakList<>();
    private final Translator translator;
    private final TextFormatter textFormatter = new TextFormatter();
    private final LanguagePreferenceProvider languagePreferenceProvider;
    private final String defaultLanguage;
    private final WeakList<TranslatedBossBar> translatedBars = new WeakList<>();

    public TranslationService(Translator translator, LanguagePreferenceProvider languagePreferenceProvider) {
        this(translator, languagePreferenceProvider, "en_us");
    }

    public TranslationService(Translator translator, LanguagePreferenceProvider languagePreferenceProvider, String defaultLanguage) {
        this.translator = translator;
        this.languagePreferenceProvider = languagePreferenceProvider;
        this.defaultLanguage = defaultLanguage;

        services.add(this);
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

    public String translate(ServerCommandSource source, String key) {
        ServerPlayerEntity player = source.getPlayer();

        if (player != null) {
            return translate(player, key);
        }

        return translator.translate(defaultLanguage, key);
    }

    public String translate(ServerCommandSource source, String key, Object... args) {
        ServerPlayerEntity player = source.getPlayer();

        if (player != null) {
            return translate(player, key, args);
        }

        return translator.translate(defaultLanguage, key, args);
    }

    public RootText translateText(ServerPlayerEntity player, String key, Object... args) {
        return translateText(getLanguage(player), key, args);
    }

    public RootText translateText(String language, String key, Object... args) {
        String raw = translator.translate(language, key);  // do not replace format specifiers just yet

        transformArgs(language, args);

        return textFormatter.formatText(raw, args);
    }

    private void transformArgs(String language, Object[] args) {
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];

            if (arg instanceof TextTranslatable translatable) {
                args[i] = translatable.translateTo(language);
            }
        }
    }

    public RootText translateText(ServerCommandSource source, String key, Object... args) {
        ServerPlayerEntity player = source.getPlayer();

        if (player != null) {
            return translateText(player, key, args);
        }

        return translateText(defaultLanguage, key, args);
    }

    public TranslatedText translateText(String key, Object... args) {
        return TranslatedText.create(language -> translateText(language, key, args), this::getLanguage);
    }

    public Partial<TranslatedBossBar, BossBarProvider> translateBossBar(Identifier id, String key, Object... args) {
        return handler -> {
            TranslatedBossBar bar = new TranslatedBossBar(handler, id, this, key, args);
            translatedBars.add(bar);
            return bar;
        };
    }

    static {
        LanguageChangedCallback.HOOK.register((player, language, reason) -> {
            for (TranslationService service : services) {
                for (TranslatedBossBar bossBar : service.translatedBars) {
                    bossBar.updatePlayerLanguage(player);
                }
            }
        });
    }
}
