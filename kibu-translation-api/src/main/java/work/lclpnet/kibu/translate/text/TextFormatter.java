package work.lclpnet.kibu.translate.text;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import javax.annotation.Nonnull;
import java.util.Formatter;
import java.util.List;

public class TextFormatter {

    private final FormatSplitter splitter = new FormatSplitter();

    @Nonnull
    public RootText formatText(String format, Object... args) {
        return formatText(format, Style.EMPTY, args);
    }

    @Nonnull
    public RootText formatText(String format, Style defaultStyle, Object... args) {
        final List<String> parts = splitter.split(format);

        final int size = parts.size();
        if (size == 0) {
            return RootText.create();
        }

        final StringBuilder formatBuffer = new StringBuilder();
        final Formatter formatter = new Formatter(formatBuffer);

        Text[] texts = new Text[size];

        for (int i = 0; i < size; i++) {
            String part = parts.get(i);

            if (!FormatSplitter.isFormatSpecifier(part)) {
                texts[i] = Text.literal(part).setStyle(defaultStyle);
                continue;
            }

            int argIndex = i / 2;
            if (argIndex >= args.length) {
                texts[i] = Text.literal(part).setStyle(defaultStyle);
                continue;
            }

            Object arg = args[argIndex];

            if (arg instanceof Text next) {
                texts[i] = next;
                continue;
            }

            if (arg instanceof FormatWrapper wrapper) {
                formatter.format(part, wrapper.getWrapped());

                String string = formatBuffer.toString();
                formatBuffer.setLength(0);  // reset buffer for reuse

                MutableText next = Text.literal(string)
                        .setStyle(wrapper.getStyle());

                texts[i] = next;

                continue;
            }

            formatter.format(part, arg);

            String string = formatBuffer.toString();
            formatBuffer.setLength(0);  // reset buffer for reuse

            MutableText next = Text.literal(string).setStyle(defaultStyle);
            texts[i] = next;
        }

        final RootText text = RootText.create();

        for (Text value : texts) {
            text.append(value);
        }

        return text;
    }
}
