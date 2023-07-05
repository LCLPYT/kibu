package work.lclpnet.kibu.translate.text;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;
import java.util.function.UnaryOperator;

public final class FormatWrapper {

    private final Object wrapped;
    private Style style;

    private FormatWrapper(Object wrapped, Style style) {
        this.wrapped = wrapped;
        this.style = style;
    }

    public static FormatWrapper styled(Object obj) {
        return styled(obj, Style.EMPTY);
    }

    public static FormatWrapper styled(Object obj, Formatting... formatting) {
        return styled(obj, Style.EMPTY.withFormatting(formatting));
    }

    public static FormatWrapper styled(Object obj, Style style) {
        return new FormatWrapper(obj, style);
    }

    public Object getWrapped() {
        return wrapped;
    }

    public Style getStyle() {
        return style;
    }

    public FormatWrapper setStyle(Style style) {
        this.style = style;
        return this;
    }

    /**
     * Updates the style of this text.
     *
     * @see Text#getStyle()
     * @see #setStyle(Style)
     *
     * @param styleUpdater the style updater
     */
    public FormatWrapper styled(UnaryOperator<Style> styleUpdater) {
        this.setStyle(styleUpdater.apply(this.getStyle()));
        return this;
    }

    /**
     * Fills the absent parts of this text's style with definitions from {@code
     * styleOverride}.
     *
     * @see Style#withParent(Style)
     *
     * @param styleOverride the style that provides definitions for absent definitions in this text's style
     */
    public FormatWrapper fillStyle(Style styleOverride) {
        this.setStyle(styleOverride.withParent(this.getStyle()));
        return this;
    }

    /**
     * Adds some formattings to this text's style.
     *
     * @param formattings an array of formattings
     */
    public FormatWrapper formatted(Formatting... formattings) {
        this.setStyle(this.getStyle().withFormatting(formattings));
        return this;
    }

    /**
     * Add a formatting to this text's style.
     *
     * @param formatting a formatting
     */
    public FormatWrapper formatted(Formatting formatting) {
        this.setStyle(this.getStyle().withFormatting(formatting));
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FormatWrapper) obj;
        return Objects.equals(this.wrapped, that.wrapped) &&
                Objects.equals(this.style, that.style);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wrapped, style);
    }

    @Override
    public String toString() {
        return "Formatted[object=%s, formatting=%s]".formatted(wrapped, style);
    }

}
