package work.lclpnet.kibu.translate.text;

import com.google.common.collect.Lists;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.UnaryOperator;

public class RootText implements Text {

    private final List<Text> siblings;
    private Style style;
    private OrderedText ordered = OrderedText.EMPTY;
    @Nullable
    private Language language;

    protected RootText(List<Text> siblings, Style style) {
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
    public TextContent getContent() {
        return TextContent.EMPTY;
    }

    @Override
    public List<Text> getSiblings() {
        return siblings;
    }

    @Override
    public OrderedText asOrderedText() {
        Language language = Language.getInstance();

        if (this.language != language) {
            this.ordered = language.reorder(this);
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

        if (o instanceof MutableText text) {
            return TextContent.EMPTY.equals(text.getContent()) && this.style.equals(text.getStyle()) && this.siblings.equals(text.getSiblings());
        }

        return false;
    }

    private void applyDefaultStyle(Text text) {
        if (text instanceof MutableText mutable) {
            mutable.setStyle(mutable.getStyle().withParent(this.getStyle()));
        } else if (text instanceof RootText root) {
            root.setStyle(root.getStyle().withParent(this.getStyle()));
        }
    }

    /**
     * Sets the style of this text.
     */
    public RootText setStyle(Style style) {
        this.style = style;

        for (Text text : siblings) {
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
        return this.append(Text.literal(text));
    }

    /**
     * Appends a text to this text's siblings.
     *
     * @param text the sibling
     */
    public RootText append(Text text) {
        applyDefaultStyle(text);
        this.siblings.add(text);
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
    public RootText styled(UnaryOperator<Style> styleUpdater) {
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
    public RootText fillStyle(Style styleOverride) {
        this.setStyle(styleOverride.withParent(this.getStyle()));
        return this;
    }

    /**
     * Adds some formattings to this text's style.
     *
     * @param formattings an array of formattings
     */
    public RootText formatted(Formatting... formattings) {
        this.setStyle(this.getStyle().withFormatting(formattings));
        return this;
    }

    /**
     * Add a formatting to this text's style.
     *
     * @param formatting a formatting
     */
    public RootText formatted(Formatting formatting) {
        this.setStyle(this.getStyle().withFormatting(formatting));
        return this;
    }
}
