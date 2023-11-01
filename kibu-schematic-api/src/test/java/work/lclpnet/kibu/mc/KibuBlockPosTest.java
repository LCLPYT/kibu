package work.lclpnet.kibu.mc;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KibuBlockPosTest {

    @Test
    void testCuboidIterator() {
        KibuBlockPos start = new KibuBlockPos(0);
        KibuBlockPos end = new KibuBlockPos(2);

        int width = end.getX() - start.getX() + 1;
        int height = end.getY() - start.getY() + 1;
        int length = end.getZ() - start.getZ() + 1;

        boolean[] checks = new boolean[width * height * length];
        Arrays.fill(checks, false);

        var iterator = KibuBlockPos.cuboidIterator(start, end);

        while (iterator.hasNext()) {
            KibuBlockPos pos = iterator.next();
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            checks[x + width * z + y * width * length] = true;
        }

        assertTrue(IntStream.range(0, checks.length)
                .mapToObj(i -> checks[i])
                .allMatch(Boolean::booleanValue));
    }

    @Test
    public void mutable_immutable_equal() {
        var immutable = new KibuBlockPos(1);
        var mutable = new KibuBlockPos.Mutable(1);

        assertEquals(immutable, mutable);
        assertEquals(mutable, immutable);
    }
}