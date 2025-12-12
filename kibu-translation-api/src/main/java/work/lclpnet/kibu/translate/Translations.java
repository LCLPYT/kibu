package work.lclpnet.kibu.translate;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import work.lclpnet.kibu.access.PlayerLanguage;
import work.lclpnet.kibu.translate.bossbar.BossBarProvider;
import work.lclpnet.kibu.translate.bossbar.TranslatedBossBar;
import work.lclpnet.kibu.translate.hook.LanguageChangedCallback;
import work.lclpnet.kibu.translate.pref.FabricLanguagePreferenceProvider;
import work.lclpnet.kibu.translate.pref.LanguagePreferenceProvider;
import work.lclpnet.kibu.translate.text.*;
import work.lclpnet.kibu.translate.util.LocaleUtil;
import work.lclpnet.kibu.translate.util.Partial;
import work.lclpnet.kibu.translate.util.WeakList;
import work.lclpnet.translations.Translator;

import java.util.Locale;

public class Translations {

    private static final WeakList<Translations> services = new WeakList<>();
    private final Translator translator;
    private final TextFormatter textFormatter = new TextFormatter();
    private final LanguagePreferenceProvider languagePreferenceProvider;
    private final String defaultLanguage;
    private final WeakList<TranslatedBossBar> translatedBars = new WeakList<>();

    public Translations(Translator translator) {
        this(translator, FabricLanguagePreferenceProvider.getInstance());
    }

    public Translations(Translator translator, LanguagePreferenceProvider languagePreferenceProvider) {
        this(translator, languagePreferenceProvider, "en_us");
    }

    public Translations(Translator translator, LanguagePreferenceProvider languagePreferenceProvider, String defaultLanguage) {
        this.translator = translator;
        this.languagePreferenceProvider = languagePreferenceProvider;
        this.defaultLanguage = defaultLanguage;

        services.add(this);
    }

    public Translator getTranslator() {
        return translator;
    }

    @NotNull
    public String getLanguage(ServerPlayer player) {
        return languagePreferenceProvider.getLanguagePreference(player)
                .orElseGet(() -> PlayerLanguage.getLanguage(player));
    }

    @NotNull
    public Locale getLocale(ServerPlayer player) {
        return LocaleUtil.getLocale(getLanguage(player));
    }

    public String translate(ServerPlayer player, String key) {
        String language = getLanguage(player);
        return translator.translate(language, key);
    }

    public String translate(ServerPlayer player, String key, Object... args) {
        String language = getLanguage(player);
        return translator.translate(language, key, args);
    }

    public String translate(String language, String key) {
        return translator.translate(language, key);
    }

    public String translate(String language, String key, Object... args) {
        return translator.translate(language, key, args);
    }

    public String translate(CommandSourceStack source, String key) {
        ServerPlayer player = source.getPlayer();

        if (player != null) {
            return translate(player, key);
        }

        return translator.translate(defaultLanguage, key);
    }

    public String translate(CommandSourceStack source, String key, Object... args) {
        ServerPlayer player = source.getPlayer();

        if (player != null) {
            return translate(player, key, args);
        }

        return translator.translate(defaultLanguage, key, args);
    }

    public RootText translateText(ServerPlayer player, String key, Object... args) {
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
                    Component text = translatable.translateTo(language);
                    Style style = wrapper.getStyle();

                    if (text instanceof RootText rootText) {
                        text = rootText.setStyle(style);
                    } else if (text instanceof MutableComponent mutableText) {
                        text = mutableText.setStyle(style);
                    }

                    modifiedArgs[i] = text;
                }
            }
        }

        return modifiedArgs;
    }

    public RootText translateText(CommandSourceStack source, String key, Object... args) {
        ServerPlayer player = source.getPlayer();

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
            for (Translations service : services) {
                for (TranslatedBossBar bossBar : service.translatedBars) {
                    bossBar.updatePlayerLanguage(player);
                }
            }
        });
    }
}
