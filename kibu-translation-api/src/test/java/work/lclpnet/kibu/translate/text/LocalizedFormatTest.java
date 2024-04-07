package work.lclpnet.kibu.translate.text;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalizedFormatTest {

    @ParameterizedTest
    @CsvSource(value = {"en_us;3.14", "de_de;3,14"}, delimiter = ';')
    void numberFormat(String language, String expected) {
        var format = LocalizedFormat.format("%.2f", Math.PI);
        String formatted = format.translateTo(language).getString();

        assertEquals(expected, formatted);
    }
}