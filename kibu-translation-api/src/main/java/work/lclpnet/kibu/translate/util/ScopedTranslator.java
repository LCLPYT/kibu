package work.lclpnet.kibu.translate.util;

import org.jetbrains.annotations.NotNull;
import work.lclpnet.translations.Translator;

import java.text.SimpleDateFormat;

public class ScopedTranslator implements Translator {

    protected final Translator parent;
    protected final String prefix;

    public ScopedTranslator(Translator parent, String prefix) {
        this.parent = parent;
        this.prefix = prefix;
    }

    @Override
    public @NotNull String translate(String locale, String key) {
        return parent.translate(locale, prefixed(key));
    }

    @Override
    public boolean hasTranslation(String locale, String key) {
        return parent.hasTranslation(locale, prefixed(key));
    }

    @Override
    public @NotNull SimpleDateFormat getDateFormat(String locale) {
        return parent.getDateFormat(locale);
    }

    @Override
    public Iterable<String> getLanguages() {
        return parent.getLanguages();
    }

    public String prefixed(String suffix) {
        return prefix + suffix;
    }

    public Translator getParent() {
        return parent;
    }
}
