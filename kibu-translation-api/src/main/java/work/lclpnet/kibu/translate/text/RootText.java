package work.lclpnet.kibu.translate.text;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.UnaryOperator;

public class RootText implements Component {

    private final List<Component> siblings;
    private Style style;
    private FormattedCharSequence ordered = FormattedCharSequence.EMPTY;
    @Nullable
    private Language language;

    protected RootText(List<Component> siblings, Style style) {
        this.siblings = siblings;
        this.style = style;
    }

    public static RootText create() {
        return new RootText(Lists.newArrayList(), Style.EMPTY);
    }

    @Override
    public Style getStyle() {
        return style;
    }

    @Override
    public ComponentContents getContents() {
        return PlainTextContents.EMPTY;
    }

    @Override
    public List<Component> getSiblings() {
        return siblings;
    }

    @Override
    public FormattedCharSequence getVisualOrderText() {
        Language language = Language.getInstance();

        if (this.language != language) {
            this.ordered = language.getVisualOrder(this);
            this.language = language;
        }

        return this.ordered;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof RootText text) {
            return this.style.equals(text.style) && this.siblings.equals(text.siblings);
        }

        if (o instanceof MutableComponent text) {
            return PlainTextContents.EMPTY.equals(text.getContents()) && this.style.equals(text.getStyle()) && this.siblings.equals(text.getSiblings());
        }

        return false;
    }

    private void applyDefaultStyle(Component text) {
        if (text instanceof MutableComponent mutable) {
            mutable.setStyle(mutable.getStyle().applyTo(this.getStyle()));
        } else if (text instanceof RootText root) {
            root.setStyle(root.getStyle().applyTo(this.getStyle()));
        }
    }

    /**
     * Sets the style of this text.
     */
    public RootText setStyle(Style style) {
        this.style = style;

        for (Component text : siblings) {
            applyDefaultStyle(text);
        }

        return this;
    }

    /**
     * Appends a literal text with content {@code text} to this text's siblings.
     *
     * @param text the literal text content
     */
    public RootText append(String text) {
        return this.append(Component.literal(text));
    }

    /**
     * Appends a text to this text's siblings.
     *
     * @param text the sibling
     */
    public RootText append(Component text) {
        applyDefaultStyle(text);
        this.siblings.add(text);
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
    public RootText withStyle(UnaryOperator<Style> styleUpdater) {
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
    public RootText withStyle(Style styleOverride) {
        this.setStyle(styleOverride.applyTo(this.getStyle()));
        return this;
    }

    /**
     * Adds some formattings to this text's style.
     *
     * @param formattings an array of formattings
     */
    public RootText withStyle(ChatFormatting... formattings) {
        this.setStyle(this.getStyle().applyFormats(formattings));
        return this;
    }

    /**
     * Add a formatting to this text's style.
     *
     * @param formatting a formatting
     */
    public RootText withStyle(ChatFormatting formatting) {
        this.setStyle(this.getStyle().applyFormat(formatting));
        return this;
    }

    /**
     * Set the color of this text's style.
     *
     * @param color The packed color int.
     */
    public RootText withColor(int color) {
        this.setStyle(this.getStyle().withColor(color));
        return this;
    }

    /**
     * Set the color of this text's style.
     *
     * @param color The text color.
     */
    public RootText withColor(TextColor color) {
        this.setStyle(this.getStyle().withColor(color));
        return this;
    }
}
