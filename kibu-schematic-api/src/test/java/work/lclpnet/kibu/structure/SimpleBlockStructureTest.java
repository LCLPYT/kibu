package work.lclpnet.kibu.structure;

import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.mc.BuiltinKibuBlockState;
import work.lclpnet.kibu.mc.KibuBlockPos;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SimpleBlockStructureTest {

    @Test
    void isWithinBox_insideEven_true() {
        var struct = new SimpleBlockStructure(0);
        struct.setBlockState(new KibuBlockPos(0), new BuiltinKibuBlockState("foo"));
        struct.setBlockState(new KibuBlockPos(1), new BuiltinKibuBlockState("bar"));

        assertAllWithin(struct, new KibuBlockPos(0), new KibuBlockPos(1));
        assertFalse(struct.isWithinBox(new KibuBlockPos(2)));
        assertFalse(struct.isWithinBox(new KibuBlockPos(-1)));
    }

    @Test
    void isWithinBox_insideOdd_true() {
        var struct = new SimpleBlockStructure(0);
        struct.setBlockState(new KibuBlockPos(0), new BuiltinKibuBlockState("foo"));
        struct.setBlockState(new KibuBlockPos(1), new BuiltinKibuBlockState("bar"));
        struct.setBlockState(new KibuBlockPos(-1), new BuiltinKibuBlockState("baz"));

        assertAllWithin(struct, new KibuBlockPos(-1), new KibuBlockPos(1));
        assertFalse(struct.isWithinBox(new KibuBlockPos(2)));
        assertFalse(struct.isWithinBox(new KibuBlockPos(-2)));
    }

    private void assertAllWithin(SimpleBlockStructure structure, KibuBlockPos from, KibuBlockPos to) {
        assertTrue(KibuBlockPos.streamCuboid(from, to).allMatch(structure::isWithinBox));
    }

    @Test
    void isOnSurface_surfaceEvent_correct() {
        KibuBlockPos start = new KibuBlockPos(0);
        KibuBlockPos end = new KibuBlockPos(3);

        var struct = new SimpleBlockStructure(0);
        struct.setBlockState(start, new BuiltinKibuBlockState("foo"));
        struct.setBlockState(end, new BuiltinKibuBlockState("bar"));

        assertTrue(struct.isOnSurface(new KibuBlockPos(0, 2, 0)));
        assertTrue(struct.isOnSurface(new KibuBlockPos(1, 0, 3)));
        assertTrue(struct.isOnSurface(new KibuBlockPos(0, 2, 1)));

        assertFalse(struct.isOnSurface(new KibuBlockPos(1, 2, 2)));
        assertFalse(struct.isOnSurface(new KibuBlockPos(2, 1, 1)));

        var inner = KibuBlockPos.streamCuboid(new KibuBlockPos(1), new KibuBlockPos(2))
                .map(KibuBlockPos::new)  // clone iteration position object
                .collect(Collectors.toUnmodifiableSet());

        KibuBlockPos.streamCuboid(start, end)
                .forEach(pos -> assertNotEquals(inner.contains(pos), struct.isOnSurface(pos)));
    }

    @Test
    void newInstance_default_dimensionsZero() {
        var struct = new SimpleBlockStructure(0);
        assertEquals(0, struct.getWidth());
        assertEquals(0, struct.getHeight());
        assertEquals(0, struct.getLength());
    }

    @Test
    void setBlockPos_sizeIncrease_correct() {
        var struct = new SimpleBlockStructure(0);

        struct.setBlockState(new KibuBlockPos(0), new BuiltinKibuBlockState("foo"));
        assertEquals(1, struct.getWidth());
        assertEquals(1, struct.getHeight());
        assertEquals(1, struct.getLength());

        struct.setBlockState(new KibuBlockPos(0, 1, 0), new BuiltinKibuBlockState("bar"));
        assertEquals(1, struct.getWidth());
        assertEquals(2, struct.getHeight());
        assertEquals(1, struct.getLength());

        struct.setBlockState(new KibuBlockPos(1, 0, 0), new BuiltinKibuBlockState("baz"));
        assertEquals(2, struct.getWidth());
        assertEquals(2, struct.getHeight());
        assertEquals(1, struct.getLength());

        struct.setBlockState(new KibuBlockPos(0, 0, 1), new BuiltinKibuBlockState("idk"));
        assertEquals(2, struct.getWidth());
        assertEquals(2, struct.getHeight());
        assertEquals(2, struct.getLength());
    }

    @Test
    void setBlockPos_sizeDecrease_correct() {
        var struct = new SimpleBlockStructure(0);

        struct.setBlockState(new KibuBlockPos(0, 1, 0), new BuiltinKibuBlockState("bar"));
        struct.setBlockState(new KibuBlockPos(1, 1, 0), new BuiltinKibuBlockState("baz"));
        assertEquals(2, struct.getWidth());
        assertEquals(1, struct.getHeight());
        assertEquals(1, struct.getLength());

        struct.setBlockState(new KibuBlockPos(0, 1, 0), null);
        assertEquals(1, struct.getWidth());
        assertEquals(1, struct.getHeight());
        assertEquals(1, struct.getLength());

        struct.setBlockState(new KibuBlockPos(1, 1, 0), BuiltinKibuBlockState.AIR);
        assertEquals(0, struct.getWidth());
        assertEquals(0, struct.getHeight());
        assertEquals(0, struct.getLength());
    }

    @Test
    void getBlockCount_empty_zero() {
        var struct = new SimpleBlockStructure(0);
        assertEquals(0, struct.getBlockCount());
    }

    @Test
    void getBlockCount_filled_correct() {
        var struct = new SimpleBlockStructure(0);

        struct.setBlockState(new KibuBlockPos(2), new BuiltinKibuBlockState("foo"));
        assertEquals(1, struct.getBlockCount());

        struct.setBlockState(new KibuBlockPos(2), new BuiltinKibuBlockState("bar"));
        assertEquals(1, struct.getBlockCount());

        struct.setBlockState(new KibuBlockPos(1), new BuiltinKibuBlockState("foo"));
        assertEquals(2, struct.getBlockCount());
    }
}