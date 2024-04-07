package work.lclpnet.kibu.translate.util;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class LocaleUtil {

    private LocaleUtil() {}

    @NotNull
    public static Locale getLocale(String language) {
        String normalized = language.toLowerCase(Locale.ROOT).replace('-', '_');

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
}
