package work.lclpnet.kibu.structure;

import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.mc.BuiltinKibuBlockState;
import work.lclpnet.kibu.mc.KibuBlockPos;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static work.lclpnet.kibu.mc.BuiltinKibuBlockState.AIR;

class ArrayBlockStructureTest {

    @Test
    void setBlockState_inside_success() {
        var struct = new ArrayBlockStructure(2, 2, 2, new KibuBlockPos(5, 5, 5), 0);

        BuiltinKibuBlockState foo = new BuiltinKibuBlockState("foo");
        BuiltinKibuBlockState bar = new BuiltinKibuBlockState("bar");

        struct.setBlockState(new KibuBlockPos(6, 6, 6), foo);
        struct.setBlockState(new KibuBlockPos(5, 5, 5), bar);

        assertEquals(foo, struct.getBlockState(new KibuBlockPos(6, 6, 6)));
        assertEquals(bar, struct.getBlockState(new KibuBlockPos(5, 5, 5)));
    }

    @Test
    void setBlockState_outside_throws() {
        var struct = new ArrayBlockStructure(2, 2, 2, new KibuBlockPos(5, 5, 5), 0);

        assertThrows(UnsupportedOperationException.class, ()
                -> struct.setBlockState(new KibuBlockPos(0, 0, 0), new BuiltinKibuBlockState("foo")));
    }

    @Test
    void getBlockState_empty_allAir() {
        var struct = new ArrayBlockStructure(4, 3, 2, new KibuBlockPos(2, 3, 4), 0);

        for (int x = 2; x <= 5; x++)
            for (int y = 3; y <= 5; y++)
                for (int z = 4; z <= 5; z++)
                    assertEquals(AIR, struct.getBlockState(new KibuBlockPos(x, y, z)));
    }

    @Test
    void getBlockState_outside_isAir() {
        var struct = new ArrayBlockStructure(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);

        assertEquals(AIR, struct.getBlockState(new KibuBlockPos(0, 0, 0)));
        assertEquals(AIR, struct.getBlockState(new KibuBlockPos(0, 1, 0)));
        assertEquals(AIR, struct.getBlockState(new KibuBlockPos(0, 0, 1)));
        assertEquals(AIR, struct.getBlockState(new KibuBlockPos(1, 0, 1)));
        assertEquals(AIR, struct.getBlockState(new KibuBlockPos(1, 1, 0)));
        assertEquals(AIR, struct.getBlockState(new KibuBlockPos(0, 1, 1)));

        assertEquals(AIR, struct.getBlockState(new KibuBlockPos(3, 0, 0)));
        assertEquals(AIR, struct.getBlockState(new KibuBlockPos(0, 3, 0)));
        assertEquals(AIR, struct.getBlockState(new KibuBlockPos(0, 0, 3)));
        assertEquals(AIR, struct.getBlockState(new KibuBlockPos(3, 0, 3)));
        assertEquals(AIR, struct.getBlockState(new KibuBlockPos(3, 3, 0)));
        assertEquals(AIR, struct.getBlockState(new KibuBlockPos(0, 3, 3)));
    }

    @Test
    void getBlockPositions_elementCount_asExpected() {
        var struct = new ArrayBlockStructure(2, 3, 4, new KibuBlockPos(1, 1, 1), 0);

        int count = 0;

        for (var ignored : struct.getBlockPositions()) {
            count++;
        }

        assertEquals(24, count);
    }

    @Test
    void getBlockPositions_ordering_isYXZ() {
        var struct = new ArrayBlockStructure(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);

        var list = new ArrayList<KibuBlockPos>();

        for (var pos : struct.getBlockPositions()) {
            list.add(pos.toImmutable());
        }

        assertEquals(List.of(
                new KibuBlockPos(1, 1, 1),
                new KibuBlockPos(1, 1, 2),
                new KibuBlockPos(2, 1, 1),
                new KibuBlockPos(2, 1, 2),
                new KibuBlockPos(1, 2, 1),
                new KibuBlockPos(1, 2, 2),
                new KibuBlockPos(2, 2, 1),
                new KibuBlockPos(2, 2, 2)
        ), list);
    }

    @Test
    void getBlockCount_initial_zero() {
        var struct = new ArrayBlockStructure(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);
        assertEquals(0, struct.getBlockCount());
    }

    @Test
    void getBlockCount_setNonAir_increase() {
        var struct = new ArrayBlockStructure(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);

        struct.setBlockState(new KibuBlockPos(1, 1, 1), new BuiltinKibuBlockState("foo"));
        assertEquals(1, struct.getBlockCount());

        struct.setBlockState(new KibuBlockPos(1, 2, 1), new BuiltinKibuBlockState("foo"));
        assertEquals(2, struct.getBlockCount());
    }

    @Test
    void getBlockCount_setTwice_onlyOnce() {
        var struct = new ArrayBlockStructure(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);

        struct.setBlockState(new KibuBlockPos(1, 1, 1), new BuiltinKibuBlockState("foo"));
        assertEquals(1, struct.getBlockCount());

        struct.setBlockState(new KibuBlockPos(1, 1, 1), new BuiltinKibuBlockState("foo"));
        assertEquals(1, struct.getBlockCount());
    }

    @Test
    void getBlockCount_setAirWasNonAir_decrease() {
        var struct = new ArrayBlockStructure(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);

        struct.setBlockState(new KibuBlockPos(1, 1, 1), new BuiltinKibuBlockState("foo"));
        struct.setBlockState(new KibuBlockPos(2, 1, 1), new BuiltinKibuBlockState("foo"));

        struct.setBlockState(new KibuBlockPos(1, 1, 1), AIR);
        assertEquals(1, struct.getBlockCount());

        struct.setBlockState(new KibuBlockPos(2, 1, 1), AIR);
        assertEquals(0, struct.getBlockCount());
    }

    @Test
    void getBlockCount_setAirWasAir_noop() {
        var struct = new ArrayBlockStructure(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);

        struct.setBlockState(new KibuBlockPos(1, 1, 1), AIR);
        assertEquals(0, struct.getBlockCount());
    }

    @Test
    void outside_samples_asExpected() {
        var struct = new ArrayBlockStructure(2, 3, 4, new KibuBlockPos(1, 1, 1), 0);

        assertTrue(struct.outside(-1, -1, -1));
        assertTrue(struct.outside(2, 3, 4));

        assertFalse(struct.outside(0, 0, 0));
        assertFalse(struct.outside(1, 2, 3));
        assertFalse(struct.outside(1, 1, 2));
        assertFalse(struct.outside(1, 2, 3));
    }
}