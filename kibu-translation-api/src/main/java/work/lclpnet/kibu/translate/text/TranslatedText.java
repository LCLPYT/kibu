package work.lclpnet.kibu.translate.text;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class TranslatedText implements TextTranslatable {

    private final Function<String, RootText> textFactory;
    private final Function<ServerPlayer, String> languageGetter;
    private Style style;
    @Nullable
    private Component prefix = null;

    private TranslatedText(Function<String, RootText> textFactory,
                           Function<ServerPlayer, String> languageGetter, Style style) {
        this.textFactory = textFactory;
        this.languageGetter = languageGetter;
        this.style = style;
    }

    public static TranslatedText create(Function<String, RootText> textFactory,
                                        Function<ServerPlayer, String> languageGetter) {
        return create(textFactory, languageGetter, Style.EMPTY);
    }

    public static TranslatedText create(Function<String, RootText> textFactory,
                                        Function<ServerPlayer, String> languageGetter, Style style) {
        return new TranslatedText(textFactory, languageGetter, style);
    }

    private String getLanguage(ServerPlayer player) {
        return languageGetter.apply(player);
    }

    public void acceptEach(Iterable<? extends ServerPlayer> players, BiConsumer<ServerPlayer, Component> action) {
        for (ServerPlayer player : players) {
            action.accept(player, textFor(player));
        }
    }

    public Component textFor(ServerPlayer player) {
        RootText text = translateFor(player);

        return prefix != null ? prefix.copy().append(text) : text;
    }

    public void sendTo(ServerPlayer player) {
        sendTo(player, false);
    }

    public void sendTo(ServerPlayer player, boolean overlay) {
        player.displayClientMessage(textFor(player), overlay);
    }

    public void sendTo(Iterable<? extends ServerPlayer> players) {
        sendTo(players, false);
    }

    public void sendTo(Iterable<? extends ServerPlayer> players, boolean overlay) {
        acceptEach(players, (player, text) -> player.sendSystemMessage(text, overlay));
    }

    public TranslatedText prefixed(MutableComponent prefix) {
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
     * @see Component#getStyle()
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
     * @see Style#applyTo(Style)
     *
     * @param styleOverride the style that provides definitions for absent definitions in this text's style
     */
    public TranslatedText fillStyle(Style styleOverride) {
        this.setStyle(styleOverride.applyTo(this.getStyle()));
        return this;
    }

    /**
     * Adds some formattings to this text's style.
     *
     * @param formattings an array of formattings
     */
    public TranslatedText formatted(ChatFormatting... formattings) {
        this.setStyle(this.getStyle().applyFormats(formattings));
        return this;
    }

    /**
     * Add a formatting to this text's style.
     *
     * @param formatting a formatting
     */
    public TranslatedText formatted(ChatFormatting formatting) {
        this.setStyle(this.getStyle().applyFormat(formatting));
        return this;
    }

    @Override
    public RootText translateTo(String language) {
        RootText text = textFactory.apply(language);

        text.setStyle(style.applyTo(text.getStyle()));

        return text;
    }

    public RootText translateFor(ServerPlayer player) {
        String language = getLanguage(player);
        return translateTo(language);
    }
}
