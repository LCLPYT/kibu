package work.lclpnet.kibu.mc;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

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
    void testCuboidIterator_correct() {
        KibuBlockPos start = new KibuBlockPos(0, 0, 0);
        KibuBlockPos end = new KibuBlockPos(1, 2, 3);

        var iterator = KibuBlockPos.cuboidIterator(start, end);

        assertEquals(new KibuBlockPos(0, 0, 0), iterator.next());
        assertEquals(new KibuBlockPos(0, 0, 1), iterator.next());
        assertEquals(new KibuBlockPos(0, 0, 2), iterator.next());
        assertEquals(new KibuBlockPos(0, 0, 3), iterator.next());
        assertEquals(new KibuBlockPos(1, 0, 0), iterator.next());
        assertEquals(new KibuBlockPos(1, 0, 1), iterator.next());
        assertEquals(new KibuBlockPos(1, 0, 2), iterator.next());
        assertEquals(new KibuBlockPos(1, 0, 3), iterator.next());
        assertEquals(new KibuBlockPos(0, 1, 0), iterator.next());
        assertEquals(new KibuBlockPos(0, 1, 1), iterator.next());
        assertEquals(new KibuBlockPos(0, 1, 2), iterator.next());
        assertEquals(new KibuBlockPos(0, 1, 3), iterator.next());
        assertEquals(new KibuBlockPos(1, 1, 0), iterator.next());
        assertEquals(new KibuBlockPos(1, 1, 1), iterator.next());
        assertEquals(new KibuBlockPos(1, 1, 2), iterator.next());
        assertEquals(new KibuBlockPos(1, 1, 3), iterator.next());
        assertEquals(new KibuBlockPos(0, 2, 0), iterator.next());
        assertEquals(new KibuBlockPos(0, 2, 1), iterator.next());
        assertEquals(new KibuBlockPos(0, 2, 2), iterator.next());
        assertEquals(new KibuBlockPos(0, 2, 3), iterator.next());
        assertEquals(new KibuBlockPos(1, 2, 0), iterator.next());
        assertEquals(new KibuBlockPos(1, 2, 1), iterator.next());
        assertEquals(new KibuBlockPos(1, 2, 2), iterator.next());
        assertEquals(new KibuBlockPos(1, 2, 3), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void mutable_immutable_equal() {
        var immutable = new KibuBlockPos(1);
        var mutable = new KibuBlockPos.Mutable(1);

        assertEquals(immutable, mutable);
        assertEquals(mutable, immutable);
    }
}