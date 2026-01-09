package work.lclpnet.kibu.map;

import net.minecraft.world.level.material.MapColor;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class MapColorUtil {

    private final int[] renderColors;

    private MapColorUtil() {
        var brightnesses = MapColor.Brightness.values();
        int colorCount = 64;  // MapColor.COLORS.length

        this.renderColors = new int[colorCount * brightnesses.length];
        Arrays.fill(renderColors, 0);

        for (int i = 0; i < colorCount; i++) {
            MapColor color = MapColor.byId(i);

            for (var brightness : brightnesses) {
                int idx = Byte.toUnsignedInt(color.getPackedId(brightness));
                int argb = color.calculateARGBColor(brightness);

                renderColors[idx] = argb;
            }
        }
    }

    private static int getArgb(boolean alpha, byte[] pixels, int i) {
        int argb;

        if (alpha) {
            argb = Byte.toUnsignedInt(pixels[i]) << 24;
            argb += Byte.toUnsignedInt(pixels[i + 1]);
            argb += Byte.toUnsignedInt(pixels[i + 2]) << 8;
            argb += Byte.toUnsignedInt(pixels[i + 3]) << 16;
        } else {
            argb = 0xFF << 24;
            argb += Byte.toUnsignedInt(pixels[i]);
            argb += Byte.toUnsignedInt(pixels[i + 1]) << 8;
            argb += Byte.toUnsignedInt(pixels[i + 2]) << 16;
        }

        return argb;
    }

    public static byte mapColor(int argb) {
        return Holder.instance.mapColor0(argb);
    }

    public static byte[] toBytes(BufferedImage image) {
        return Holder.instance.toBytes0(image);
    }

    private static double colorDifference(int x, int y) {
        int xr = (x >> 16) & 0xFF, xg = (x >> 8) & 0xFF, xb = x & 0xFF;
        int yr = (y >> 16) & 0xFF, yg = (y >> 8) & 0xFF, yb = y & 0xFF;

        // use weighted Euclidean distance https://www.compuphase.com/cmetric.htm

        double rMean = (xr + yr) * 0.5;
        double r = xr - yr, g = xg - yg, b = xb - yb;

        double weightR = 2.0 + rMean / 256.0;
        double weightG = 4.0;
        double weightB = 2.0 + (255.0 - rMean) / 256.0;

        return weightR * r * r + weightG * g * g + weightB * b * b;
    }

    public byte mapColor0(int argb) {
        int alpha = (argb >> 24) & 0xFF;
        if (alpha < 128) return 0;  // alpha threshold

        int index = 0;
        double best = Double.MAX_VALUE;

        for (int i = 4; i < renderColors.length; i++) {
            int renderColor = renderColors[i];
            int renderAlpha = (renderColor >> 24) & 0xFF;

            if (renderAlpha < 128) continue;  // color would be invisible

            double distance = colorDifference(argb, renderColor);

            if (distance < best) {
                best = distance;
                index = i;
            }
        }

        return (byte) index;
    }

    public byte[] toBytes0(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
        byte[] data = new byte[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = pixels[y * width + x];

                data[y * width + x] = mapColor0(argb);
            }
        }

        return data;
    }

    private static class Holder {
        private static final MapColorUtil instance = new MapColorUtil();
    }
}
