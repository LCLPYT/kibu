package work.lclpnet.kibu.translate.text;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

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

    public static FormatWrapper styled(Object obj, ChatFormatting... formatting) {
        return styled(obj, Style.EMPTY.applyFormats(formatting));
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
     * @see Component#getStyle()
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
     * @see Style#applyTo(Style)
     *
     * @param styleOverride the style that provides definitions for absent definitions in this text's style
     */
    public FormatWrapper fillStyle(Style styleOverride) {
        this.setStyle(styleOverride.applyTo(this.getStyle()));
        return this;
    }

    /**
     * Adds some formattings to this text's style.
     *
     * @param formattings an array of formattings
     */
    public FormatWrapper formatted(ChatFormatting... formattings) {
        this.setStyle(this.getStyle().applyFormats(formattings));
        return this;
    }

    /**
     * Add a formatting to this text's style.
     *
     * @param formatting a formatting
     */
    public FormatWrapper formatted(ChatFormatting formatting) {
        this.setStyle(this.getStyle().applyFormat(formatting));
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
