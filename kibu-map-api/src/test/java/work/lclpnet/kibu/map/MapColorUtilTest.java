package work.lclpnet.kibu.map;

import net.minecraft.block.MapColor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapColorUtilTest {

    @ParameterizedTest
    @MethodSource("provideColorBrightnessArgs")
    void mapColor(MapColor color, MapColor.Brightness brightness) {
        int renderColor = color.getRenderColor(brightness);
        byte mappedColor = MapColorUtil.mapColor(renderColor);
        byte expected = renderColor == 0 ? 0 : color.getRenderColorByte(brightness);

        assertEquals(expected, mappedColor);
    }

    private static Stream<Arguments> provideColorBrightnessArgs() {
        return IntStream.range(0, 64)
                .mapToObj(MapColor::get)
                .flatMap(color -> Arrays.stream(MapColor.Brightness.values())
                        .map(brightness -> Arguments.of(color, brightness)));
    }
}