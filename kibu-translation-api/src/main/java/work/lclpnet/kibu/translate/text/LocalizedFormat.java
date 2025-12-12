package work.lclpnet.kibu.translate.text;

import net.minecraft.network.chat.Component;
import work.lclpnet.kibu.translate.util.LocaleUtil;

import java.util.Locale;

public class LocalizedFormat implements TextTranslatable {

    private final String format;
    private final Object[] args;

    public LocalizedFormat(String format, Object[] args) {
        this.format = format;
        this.args = args;
    }

    @Override
    public Component translateTo(String language) {
        Locale locale = LocaleUtil.getLocale(language);
        String str = String.format(locale, format, args);

        return Component.literal(str);
    }

    /**
     *
     * @param format A {@link java.util.Formatter} string.
     * @param args {@link java.util.Formatter} arguments.
     * @return A {@link LocalizedFormat} instance representing the format for an arbitrary locale.
     */
    public static LocalizedFormat format(String format, Object... args) {
        return new LocalizedFormat(format, args);
    }
}
