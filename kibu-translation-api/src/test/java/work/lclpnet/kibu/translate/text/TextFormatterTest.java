package work.lclpnet.kibu.translate.text;

import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import org.junit.jupiter.api.Test;

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

        RootText text = service.formatText("%s bar", Text.literal("Hello").formatted(Formatting.BLUE)).formatted(Formatting.YELLOW);
        assertEquals("Hello bar", text.getString());
        assertEquals(2, text.getSiblings().size());
        assertEquals("#5555FFHello#FFFF55 bar", debugString(text));
    }

    @Test
    void formatText_suffix() {
        var service = new TextFormatter();

        RootText text = service.formatText("Hello %s", "world").formatted(Formatting.BOLD);
        assertEquals("Hello world", text.getString());
        assertEquals(2, text.getSiblings().size());
        assertEquals("#FFFFFF§lHello #FFFFFF§lworld", debugString(text));
    }

    @Test
    void formatText_styledArg() {
        var service = new TextFormatter();

        RootText text = service.formatText("Count %.2f", styled(Math.PI, Formatting.YELLOW)).formatted(Formatting.GREEN, Formatting.BOLD);
        assertEquals("Count 3.14", text.getString());
        assertEquals(2, text.getSiblings().size());
        assertEquals("#55FF55§lCount #FFFF55§l3.14", debugString(text));
    }

    private String debugString(Text text) {
        StringBuilder builder = new StringBuilder();

        final String string;
        TextContent content = text.getContent();

        if (content instanceof LiteralTextContent literal) {
            string = literal.string();
        } else {
            string = content.toString();
        }

        if (!string.isEmpty() && text.getContent() != TextContent.EMPTY) {
            Style style = text.getStyle();
            TextColor color = style.getColor();
            builder.append(color == null ? "#FFFFFF" : color.getHexCode());
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
