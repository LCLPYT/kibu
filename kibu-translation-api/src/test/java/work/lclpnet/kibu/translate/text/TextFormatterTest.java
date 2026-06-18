package work.lclpnet.kibu.translate.text;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.PlainTextContents;
import org.junit.jupiter.api.Test;

import static net.minecraft.ChatFormatting.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static work.lclpnet.kibu.translate.text.FormatWrapper.styled;

public class TextFormatterTest {

    @Test
    void formatText_noFormatting() {
        var service = new TextFormatter();

        RootText text = service.formatText("Hello %s world", "foo");
        assertEquals("Hello foo world", text.getString());
        assertEquals(3, text.getSiblings().size());
        assertEquals("#FFFFFFHello #FFFFFFfoo#FFFFFF world", debugString(text));
    }

    @Test
    void formatText_prefix() {
        var service = new TextFormatter();

        RootText text = service.formatText("%s bar", "foo");
        assertEquals("foo bar", text.getString());
        assertEquals(2, text.getSiblings().size());
        assertEquals("#FFFFFFfoo#FFFFFF bar", debugString(text));
    }

    @Test
    void formatText_prefix_textArg() {
        var service = new TextFormatter();

        RootText text = service.formatText("%s bar", Component.literal("Hello").withStyle(BLUE)).withStyle(YELLOW);
        assertEquals("Hello bar", text.getString());
        assertEquals(2, text.getSiblings().size());
        assertEquals("#5555FFHello#FFFF55 bar", debugString(text));
    }

    @Test
    void formatText_suffix() {
        var service = new TextFormatter();

        RootText text = service.formatText("Hello %s", "world").withStyle(BOLD);
        assertEquals("Hello world", text.getString());
        assertEquals(2, text.getSiblings().size());
        assertEquals("#FFFFFF§lHello #FFFFFF§lworld", debugString(text));
    }

    @Test
    void formatText_styledArg() {
        var service = new TextFormatter();

        RootText text = service.formatText("Count %.2f", styled(Math.PI, YELLOW)).withStyle(GREEN, BOLD);
        assertEquals("Count 3.14", text.getString());
        assertEquals(2, text.getSiblings().size());
        assertEquals("#55FF55§lCount #FFFF55§l3.14", debugString(text));
    }

    @Test
    void formatText_insufficientArgs() {
        var service = new TextFormatter();

        String text = service.formatText("%s: %s", 123).getString();
        assertEquals("123: %s", text);
    }

    @Test
    void formatText_offByOne() {
        var service = new TextFormatter();

        String text = service.formatText("Hi \"%s\", this is \"%s\"", "Marc", "Paul").getString();

        assertEquals("Hi \"Marc\", this is \"Paul\"", text);
    }

    @Test
    void formatText_textInFormatWrapper() {
        var service = new TextFormatter();

        // put in a text that has formatting and override it with the FormatWrapper style
        FormatWrapper wrapper = styled(Component.literal("test").withStyle(YELLOW, BOLD), GREEN);

        RootText text = service.formatText("Test %s hello", wrapper);

        // the final substitution should be the "base" style, overridden using the FormatWrapper style
        assertEquals("#FFFFFFTest #55FF55§ltest#FFFFFF hello", debugString(text));
    }

    private String debugString(Component text) {
        StringBuilder builder = new StringBuilder();

        final String string;
        ComponentContents content = text.getContents();

        if (content instanceof PlainTextContents literal) {
            string = literal.text();
        } else {
            string = content.toString();
        }

        if (!string.isEmpty() && text.getContents() != PlainTextContents.EMPTY) {
            Style style = text.getStyle();
            TextColor color = style.getColor();
            builder.append(color == null ? "#FFFFFF" : color.formatValue());
            if (style.isBold()) builder.append("§l");
            if (style.isItalic()) builder.append("§o");
            if (style.isObfuscated()) builder.append("§k");
            if (style.isStrikethrough()) builder.append("§m");
            if (style.isUnderlined()) builder.append("§n");

            builder.append(string);
        }

        text.getSiblings().forEach(sibling -> builder.append(debugString(sibling)));

        return builder.toString();
    }
}
