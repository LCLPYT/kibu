package work.lclpnet.kibu.translate.text;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class TranslatedText {

    private final Function<ServerPlayerEntity, RootText> textFactory;
    private Style style;
    @Nullable
    private Text prefix = null;

    private TranslatedText(Function<ServerPlayerEntity, RootText> textFactory, Style style) {
        this.textFactory = textFactory;
        this.style = style;
    }

    public static TranslatedText create(Function<ServerPlayerEntity, RootText> textFactory) {
        return create(textFactory, Style.EMPTY);
    }

    public static TranslatedText create(Function<ServerPlayerEntity, RootText> textFactory, Style style) {
        return new TranslatedText(textFactory, style);
    }

    public void acceptEach(Iterable<ServerPlayerEntity> players, BiConsumer<ServerPlayerEntity, Text> action) {
        for (ServerPlayerEntity player : players) {
            RootText text = textFactory.apply(player);
            text.setStyle(style.withParent(text.getStyle()));

            Text result = prefix != null ? prefix.copy().append(text) : text;

            action.accept(player, result);
        }
    }

    public void sendTo(Iterable<ServerPlayerEntity> players) {
        sendTo(players, false);
    }

    public void sendTo(Iterable<ServerPlayerEntity> players, boolean overlay) {
        acceptEach(players, (player, text) -> player.sendMessageToClient(text, overlay));
    }

    public TranslatedText prefixed(MutableText prefix) {
        this.prefix = prefix;
        return this;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    /**
     * Updates the style of this text.
     *
     * @see Text#getStyle()
     * @see #setStyle(Style)
     *
     * @param styleUpdater the style updater
     */
    public TranslatedText styled(UnaryOperator<Style> styleUpdater) {
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
    public TranslatedText fillStyle(Style styleOverride) {
        this.setStyle(styleOverride.withParent(this.getStyle()));
        return this;
    }

    /**
     * Adds some formattings to this text's style.
     *
     * @param formattings an array of formattings
     */
    public TranslatedText formatted(Formatting... formattings) {
        this.setStyle(this.getStyle().withFormatting(formattings));
        return this;
    }

    /**
     * Add a formatting to this text's style.
     *
     * @param formatting a formatting
     */
    public TranslatedText formatted(Formatting formatting) {
        this.setStyle(this.getStyle().withFormatting(formatting));
        return this;
    }
}
