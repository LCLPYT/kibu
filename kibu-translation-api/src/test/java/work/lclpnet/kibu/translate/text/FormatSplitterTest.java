package work.lclpnet.kibu.translate.text;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormatSplitterTest {

    @Test
    void split_normal() {
        var parts = new FormatSplitter().split("Hello %s world");

        assertEquals(List.of("Hello ", "%s", " world"), parts);
    }

    @Test
    void split_prefix() {
        var parts = new FormatSplitter().split("%.2f bar test");

        assertEquals(List.of("%.2f", " bar test"), parts);
    }

    @Test
    void split_suffix() {
        var parts = new FormatSplitter().split("foo bar %d");

        assertEquals(List.of("foo bar ", "%d"), parts);
    }

    @Test
    void split_multi() {
        var parts = new FormatSplitter().split("%s %.20f %d");

        assertEquals(List.of("%s", " ", "%.20f", " ", "%d"), parts);
    }

    @Test
    void split_empty() {
        var parts = new FormatSplitter().split("");
        assertEquals(List.of(), parts);
    }

    @Test
    void split_offByOne() {
        var parts = new FormatSplitter().split("Hi \"%s\", this is \"%s\"");

        assertEquals(List.of("Hi \"", "%s", "\", this is \"", "%s", "\""), parts);
    }
}