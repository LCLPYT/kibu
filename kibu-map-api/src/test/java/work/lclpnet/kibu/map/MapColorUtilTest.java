package work.lclpnet.kibu.map;

import net.minecraft.block.MapColor;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MapColorUtilTest {

    @ParameterizedTest
    @MethodSource("provideColorBrightnessArgs")
    void mapColor(MapColor color, MapColor.Brightness brightness) {
        int renderColor = MapColorUtil.abgr2argb(color.getRenderColor(brightness));
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

    @Test
    void testImageMapping() throws IOException {
        Path dir = Path.of("src", "test", "resources", "img");
        Path src = dir.resolve("test.png");
        Path ref = dir.resolve("test.dat");

        BufferedImage img;

        try (var in = Files.newInputStream(src)) {
            img = ImageIO.read(in);
        }

        NbtCompound nbt;

        try (var in = Files.newInputStream(ref)) {
            nbt = NbtIo.readCompressed(in);
        }

        NbtCompound data = nbt.getCompound("data");
        assertTrue(data.contains("colors", NbtElement.BYTE_ARRAY_TYPE));

        byte[] expected = data.getByteArray("colors");

        byte[] actual = MapColorUtil.toBytes(img);

        assertArrayEquals(expected, actual);
    }
}