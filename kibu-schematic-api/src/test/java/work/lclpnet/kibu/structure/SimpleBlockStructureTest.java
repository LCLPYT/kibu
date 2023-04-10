package work.lclpnet.kibu.structure;

import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.mc.BlockPos;
import work.lclpnet.kibu.mc.BuiltinBlockState;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SimpleBlockStructureTest {

    @Test
    void isWithinBox_insideEven_true() {
        var struct = new SimpleBlockStructure(0);
        struct.setBlockState(new BlockPos(0), new BuiltinBlockState("foo"));
        struct.setBlockState(new BlockPos(1), new BuiltinBlockState("bar"));

        assertAllWithin(struct, new BlockPos(0), new BlockPos(1));
        assertFalse(struct.isWithinBox(new BlockPos(2)));
        assertFalse(struct.isWithinBox(new BlockPos(-1)));
    }

    @Test
    void isWithinBox_insideOdd_true() {
        var struct = new SimpleBlockStructure(0);
        struct.setBlockState(new BlockPos(0), new BuiltinBlockState("foo"));
        struct.setBlockState(new BlockPos(1), new BuiltinBlockState("bar"));
        struct.setBlockState(new BlockPos(-1), new BuiltinBlockState("baz"));

        assertAllWithin(struct, new BlockPos(-1), new BlockPos(1));
        assertFalse(struct.isWithinBox(new BlockPos(2)));
        assertFalse(struct.isWithinBox(new BlockPos(-2)));
    }

    private void assertAllWithin(SimpleBlockStructure structure, BlockPos from, BlockPos to) {
        assertTrue(BlockPos.streamCuboid(from, to).allMatch(structure::isWithinBox));
    }

    @Test
    void isOnSurface_surfaceEvent_correct() {
        BlockPos start = new BlockPos(0);
        BlockPos end = new BlockPos(3);

        var struct = new SimpleBlockStructure(0);
        struct.setBlockState(start, new BuiltinBlockState("foo"));
        struct.setBlockState(end, new BuiltinBlockState("bar"));

        assertTrue(struct.isOnSurface(new BlockPos(0, 2, 0)));
        assertTrue(struct.isOnSurface(new BlockPos(1, 0, 3)));
        assertTrue(struct.isOnSurface(new BlockPos(0, 2, 1)));

        assertFalse(struct.isOnSurface(new BlockPos(1, 2, 2)));
        assertFalse(struct.isOnSurface(new BlockPos(2, 1, 1)));

        var inner = BlockPos.streamCuboid(new BlockPos(1), new BlockPos(2))
                .map(BlockPos::new)  // clone iteration position object
                .collect(Collectors.toUnmodifiableSet());

        BlockPos.streamCuboid(start, end)
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

        struct.setBlockState(new BlockPos(0), new BuiltinBlockState("foo"));
        assertEquals(1, struct.getWidth());
        assertEquals(1, struct.getHeight());
        assertEquals(1, struct.getLength());

        struct.setBlockState(new BlockPos(0, 1, 0), new BuiltinBlockState("bar"));
        assertEquals(1, struct.getWidth());
        assertEquals(2, struct.getHeight());
        assertEquals(1, struct.getLength());

        struct.setBlockState(new BlockPos(1, 0, 0), new BuiltinBlockState("baz"));
        assertEquals(2, struct.getWidth());
        assertEquals(2, struct.getHeight());
        assertEquals(1, struct.getLength());

        struct.setBlockState(new BlockPos(0, 0, 1), new BuiltinBlockState("idk"));
        assertEquals(2, struct.getWidth());
        assertEquals(2, struct.getHeight());
        assertEquals(2, struct.getLength());
    }

    @Test
    void setBlockPos_sizeDecrease_correct() {
        var struct = new SimpleBlockStructure(0);

        struct.setBlockState(new BlockPos(0, 1, 0), new BuiltinBlockState("bar"));
        struct.setBlockState(new BlockPos(1, 1, 0), new BuiltinBlockState("baz"));
        assertEquals(2, struct.getWidth());
        assertEquals(1, struct.getHeight());
        assertEquals(1, struct.getLength());

        struct.setBlockState(new BlockPos(0, 1, 0), null);
        assertEquals(1, struct.getWidth());
        assertEquals(1, struct.getHeight());
        assertEquals(1, struct.getLength());

        struct.setBlockState(new BlockPos(1, 1, 0), BuiltinBlockState.AIR);
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

        struct.setBlockState(new BlockPos(2), new BuiltinBlockState("foo"));
        assertEquals(1, struct.getBlockCount());

        struct.setBlockState(new BlockPos(2), new BuiltinBlockState("bar"));
        assertEquals(1, struct.getBlockCount());

        struct.setBlockState(new BlockPos(1), new BuiltinBlockState("foo"));
        assertEquals(2, struct.getBlockCount());
    }

    @Test
    void isEmpty_empty_true() {
        var struct = new SimpleBlockStructure(0);
        assertTrue(struct.isEmpty());
    }

    @Test
    void isEmpty_notEmpty_false() {
        var struct = new SimpleBlockStructure(0);
        struct.setBlockState(new BlockPos(2), new BuiltinBlockState("foo"));
        assertFalse(struct.isEmpty());
    }
}