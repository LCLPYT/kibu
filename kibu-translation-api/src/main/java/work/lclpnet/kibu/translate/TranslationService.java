package work.lclpnet.kibu.translate;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import work.lclpnet.kibu.access.PlayerLanguage;
import work.lclpnet.kibu.translate.bossbar.BossBarProvider;
import work.lclpnet.kibu.translate.bossbar.TranslatedBossBar;
import work.lclpnet.kibu.translate.hook.LanguageChangedCallback;
import work.lclpnet.kibu.translate.pref.LanguagePreferenceProvider;
import work.lclpnet.kibu.translate.text.*;
import work.lclpnet.kibu.translate.util.Partial;
import work.lclpnet.kibu.translate.util.WeakList;
import work.lclpnet.translations.Translator;

import javax.annotation.Nonnull;
import java.util.Locale;

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

    @Nonnull
    public Locale getLocale(ServerPlayerEntity player) {
        String normalized = getLanguage(player).toLowerCase(Locale.ROOT).replace('-', '_');

        return switch (normalized) {
            case "de_de", "de_at", "de_ch" -> Locale.GERMAN;
            case "fr_fr", "fr_ca", "fr_be", "fr_ch" -> Locale.FRENCH;
            case "ja_jp" -> Locale.JAPANESE;
            case "ko_kr" -> Locale.KOREAN;
            case "it_it", "it_ch" -> Locale.ITALIAN;
            case "zh_cn", "zh_hk", "zh_tw" -> Locale.CHINESE;
            default -> Locale.ENGLISH;
        };
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

        Object[] transformed = transformArgs(language, args);

        return textFormatter.formatText(raw, transformed);
    }

    private Object[] transformArgs(String language, final Object[] args) {
        if (args.length == 0) {
            return args;
        }

        Object[] modifiedArgs = new Object[args.length];

        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];

            modifiedArgs[i] = arg;

            if (arg instanceof TextTranslatable translatable) {
                modifiedArgs[i] = translatable.translateTo(language);
            } else if (arg instanceof FormatWrapper wrapper) {
                if (wrapper.getWrapped() instanceof TextTranslatable translatable) {
                    Text text = translatable.translateTo(language);
                    Style style = wrapper.getStyle();

                    if (text instanceof RootText rootText) {
                        text = rootText.setStyle(style);
                    } else if (text instanceof MutableText mutableText) {
                        text = mutableText.setStyle(style);
                    }

                    modifiedArgs[i] = text;
                }
            }
        }

        return modifiedArgs;
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
