package work.lclpnet.kibu.translate.text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatSplitter {

    public static final Pattern formatPattern = Pattern.compile("%(\\d+\\$)?([-#+ 0,(<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])");

    public List<String> split(String string) {
        final List<String> parts = new ArrayList<>();
        final Matcher matcher = formatPattern.matcher(string);

        int cursor = 0;

        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            if (cursor < start) {
                parts.add(string.substring(cursor, start));
            }

            parts.add(matcher.group());

            cursor = end;
        }

        if (cursor < string.length()) {
            parts.add(string.substring(cursor));
        }

        return parts;
    }

    public static boolean isFormatSpecifier(String s) {
        return formatPattern.asMatchPredicate().test(s);
    }
}
