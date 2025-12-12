package work.lclpnet.kibu.translate.text;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;

import java.util.Formatter;
import java.util.List;

public class TextFormatter {

    private final FormatSplitter splitter = new FormatSplitter();

    @NotNull
    public RootText formatText(String format, Object... args) {
        return formatText(format, Style.EMPTY, args);
    }

    @NotNull
    public RootText formatText(String format, Style defaultStyle, Object... args) {
        final List<String> parts = splitter.split(format);

        final int size = parts.size();
        if (size == 0) {
            return RootText.create();
        }

        final StringBuilder formatBuffer = new StringBuilder();
        final Formatter formatter = new Formatter(formatBuffer);

        Component[] texts = new Component[size];

        for (int i = 0; i < size; i++) {
            String part = parts.get(i);

            if (!FormatSplitter.isFormatSpecifier(part)) {
                texts[i] = Component.literal(part).setStyle(defaultStyle);
                continue;
            }

            int argIndex = i / 2;
            if (argIndex >= args.length) {
                texts[i] = Component.literal(part).setStyle(defaultStyle);
                continue;
            }

            Object arg = args[argIndex];

            if (arg instanceof Component next) {
                texts[i] = next;
                continue;
            }

            if (arg instanceof FormatWrapper wrapper) {
                Object wrapped = wrapper.getWrapped();

                // if the wrapped object is a Text, append it directly
                if (wrapped instanceof Component next) {
                    texts[i] = next.copy().withStyle(wrapper.getStyle());
                    continue;
                }

                formatter.format(part, wrapped);

                String string = formatBuffer.toString();
                formatBuffer.setLength(0);  // reset buffer for reuse

                MutableComponent next = Component.literal(string)
                        .setStyle(wrapper.getStyle());

                texts[i] = next;

                continue;
            }

            formatter.format(part, arg);

            String string = formatBuffer.toString();
            formatBuffer.setLength(0);  // reset buffer for reuse

            MutableComponent next = Component.literal(string).setStyle(defaultStyle);
            texts[i] = next;
        }

        final RootText text = RootText.create();

        for (Component value : texts) {
            text.append(value);
        }

        return text;
    }
}
