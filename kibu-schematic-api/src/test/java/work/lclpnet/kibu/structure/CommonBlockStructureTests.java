package work.lclpnet.kibu.structure;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import work.lclpnet.kibu.mc.BuiltinKibuBlockState;
import work.lclpnet.kibu.mc.KibuBlockPos;
import work.lclpnet.kibu.mc.TestKibuBlockEntity;
import work.lclpnet.kibu.mc.TestKibuEntity;
import work.lclpnet.kibu.schematic.api.BlockStructureFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static work.lclpnet.kibu.mc.BuiltinKibuBlockState.AIR;

public class CommonBlockStructureTests {

    @ParameterizedTest
    @MethodSource("structures")
    void getBlockEntity_nothing_null(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(0, 0, 0), 0);
        assertNull(struct.getBlockEntity(new KibuBlockPos(0, 0, 0)));
    }

    @ParameterizedTest
    @MethodSource("structures")
    void getBlockEntity_stone_null(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(0, 0, 0), 0);
        KibuBlockPos pos = new KibuBlockPos(0, 0, 0);
        struct.setBlockState(pos, new BuiltinKibuBlockState("stone"));
        assertNull(struct.getBlockEntity(pos));
    }

    @ParameterizedTest
    @MethodSource("structures")
    void setBlockEntity_noBlockState_noop(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(0, 0, 0), 0);
        KibuBlockPos pos = new KibuBlockPos(0, 0, 0);
        struct.setBlockEntity(pos, TestKibuBlockEntity.of("test", pos));
        assertNull(struct.getBlockEntity(pos));
    }

    @ParameterizedTest
    @MethodSource("structures")
    void setBlockEntity_withBlockState_success(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(0, 0, 0), 0);
        KibuBlockPos pos = new KibuBlockPos(0, 0, 0);
        struct.setBlockState(pos, new BuiltinKibuBlockState("test"));

        TestKibuBlockEntity blockEntity = TestKibuBlockEntity.of("test", pos);
        struct.setBlockEntity(pos, blockEntity);

        assertEquals(blockEntity, struct.getBlockEntity(pos));
    }

    @ParameterizedTest
    @MethodSource("structures")
    void getBlockEntityCount_initial_zero(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(0, 0, 0), 0);
        assertEquals(0, struct.getBlockEntityCount());
    }

    @ParameterizedTest
    @MethodSource("structures")
    void getBlockEntityCount_setNonNull_increase(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(0, 0, 0), 0);

        KibuBlockPos pos = new KibuBlockPos(0, 0, 0);
        KibuBlockPos pos2 = new KibuBlockPos(0, 1, 0);

        struct.setBlockState(pos, new BuiltinKibuBlockState("test"));
        struct.setBlockState(pos2, new BuiltinKibuBlockState("foo"));

        struct.setBlockEntity(pos, TestKibuBlockEntity.of("test", pos));
        assertEquals(1, struct.getBlockEntityCount());

        struct.setBlockEntity(pos2, TestKibuBlockEntity.of("foo", pos2));
        assertEquals(2, struct.getBlockEntityCount());
    }

    @ParameterizedTest
    @MethodSource("structures")
    void getBlockEntityCount_setNull_decrease(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(0, 0, 0), 0);

        KibuBlockPos pos = new KibuBlockPos(0, 0, 0);
        KibuBlockPos pos2 = new KibuBlockPos(0, 1, 0);

        struct.setBlockState(pos, new BuiltinKibuBlockState("test"));
        struct.setBlockState(pos2, new BuiltinKibuBlockState("foo"));

        struct.setBlockEntity(pos, TestKibuBlockEntity.of("test", pos));
        struct.setBlockEntity(pos2, TestKibuBlockEntity.of("foo", pos2));

        struct.setBlockEntity(pos, null);
        assertEquals(1, struct.getBlockEntityCount());

        struct.setBlockEntity(pos2, null);
        assertEquals(0, struct.getBlockEntityCount());
    }

    @ParameterizedTest
    @MethodSource("structures")
    void getBlockEntityCount_setSameNull_noop(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(0, 0, 0), 0);

        KibuBlockPos pos = new KibuBlockPos(0, 0, 0);

        struct.setBlockState(pos, new BuiltinKibuBlockState("foo"));
        struct.setBlockEntity(pos, TestKibuBlockEntity.of("foo", pos));

        struct.setBlockEntity(pos, null);
        assertEquals(0, struct.getBlockEntityCount());

        struct.setBlockEntity(pos, null);
        assertEquals(0, struct.getBlockEntityCount());
    }

    @ParameterizedTest
    @MethodSource("structures")
    void getBlockEntityCount_setSameNonNull_noop(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(0, 0, 0), 0);

        KibuBlockPos pos = new KibuBlockPos(0, 0, 0);

        struct.setBlockState(pos, new BuiltinKibuBlockState("foo"));

        struct.setBlockEntity(pos, TestKibuBlockEntity.of("foo", pos));
        assertEquals(1, struct.getBlockEntityCount());

        struct.setBlockEntity(pos, TestKibuBlockEntity.of("foo", pos));
        assertEquals(1, struct.getBlockEntityCount());
    }

    @ParameterizedTest
    @MethodSource("structures")
    void getBlockEntityCount_setStateToAir_decrease(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(0, 0, 0), 0);

        KibuBlockPos pos = new KibuBlockPos(0, 0, 0);

        struct.setBlockState(pos, new BuiltinKibuBlockState("foo"));
        struct.setBlockEntity(pos, TestKibuBlockEntity.of("foo", pos));

        assertEquals(1, struct.getBlockEntityCount());

        struct.setBlockState(pos, BuiltinKibuBlockState.AIR);
        assertEquals(0, struct.getBlockEntityCount());
    }

    @ParameterizedTest
    @MethodSource("structures")
    void getBlockEntityCount_setStateToDifferent_decrease(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(0, 0, 0), 0);

        KibuBlockPos pos = new KibuBlockPos(0, 0, 0);

        struct.setBlockState(pos, new BuiltinKibuBlockState("foo"));
        struct.setBlockEntity(pos, TestKibuBlockEntity.of("foo", pos));

        assertEquals(1, struct.getBlockEntityCount());

        struct.setBlockState(pos, new BuiltinKibuBlockState("bar"));
        assertEquals(0, struct.getBlockEntityCount());
    }

    @ParameterizedTest
    @MethodSource("structures")
    void getBlockEntityCount_setBlockEntityToDifferent_noop(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(0, 0, 0), 0);

        KibuBlockPos pos = new KibuBlockPos(0, 0, 0);

        struct.setBlockState(pos, new BuiltinKibuBlockState("foo"));
        struct.setBlockEntity(pos, TestKibuBlockEntity.of("foo", pos));

        assertEquals(1, struct.getBlockEntityCount());

        struct.setBlockEntity(pos, TestKibuBlockEntity.of("bar", pos));
        assertEquals(1, struct.getBlockEntityCount());
    }

    @ParameterizedTest
    @MethodSource("structures")
    void setBlockState_changeState_blockStateRemoved(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(0, 0, 0), 0);

        KibuBlockPos pos = new KibuBlockPos(0, 0, 0);
        struct.setBlockState(pos, new BuiltinKibuBlockState("foo"));

        TestKibuBlockEntity blockEntity = TestKibuBlockEntity.of("foo", pos);
        struct.setBlockEntity(pos, blockEntity);

        assertEquals(blockEntity, struct.getBlockEntity(pos));

        struct.setBlockState(pos, new BuiltinKibuBlockState("bar"));
        assertNull(struct.getBlockEntity(pos));
    }

    @ParameterizedTest
    @MethodSource("structures")
    void setBlockState_changeStateAir_blockStateRemoved(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(0, 0, 0), 0);

        KibuBlockPos pos = new KibuBlockPos(0, 0, 0);
        struct.setBlockState(pos, new BuiltinKibuBlockState("foo"));

        TestKibuBlockEntity blockEntity = TestKibuBlockEntity.of("foo", pos);
        struct.setBlockEntity(pos, blockEntity);

        assertEquals(blockEntity, struct.getBlockEntity(pos));

        struct.setBlockState(pos, BuiltinKibuBlockState.AIR);
        assertNull(struct.getBlockEntity(pos));
    }

    @ParameterizedTest
    @MethodSource("structures")
    void getBlockCount_initial_zero(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);
        assertEquals(0, struct.getBlockCount());
    }

    @ParameterizedTest
    @MethodSource("structures")
    void getBlockCount_setNonAir_increase(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);

        struct.setBlockState(new KibuBlockPos(1, 1, 1), new BuiltinKibuBlockState("foo"));
        assertEquals(1, struct.getBlockCount());

        struct.setBlockState(new KibuBlockPos(1, 2, 1), new BuiltinKibuBlockState("foo"));
        assertEquals(2, struct.getBlockCount());
    }

    @ParameterizedTest
    @MethodSource("structures")
    void getBlockCount_setTwice_onlyOnce(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);

        struct.setBlockState(new KibuBlockPos(1, 1, 1), new BuiltinKibuBlockState("foo"));
        assertEquals(1, struct.getBlockCount());

        struct.setBlockState(new KibuBlockPos(1, 1, 1), new BuiltinKibuBlockState("foo"));
        assertEquals(1, struct.getBlockCount());
    }

    @ParameterizedTest
    @MethodSource("structures")
    void getBlockCount_setAirWasNonAir_decrease(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);

        struct.setBlockState(new KibuBlockPos(1, 1, 1), new BuiltinKibuBlockState("foo"));
        struct.setBlockState(new KibuBlockPos(2, 1, 1), new BuiltinKibuBlockState("foo"));

        struct.setBlockState(new KibuBlockPos(1, 1, 1), AIR);
        assertEquals(1, struct.getBlockCount());

        struct.setBlockState(new KibuBlockPos(2, 1, 1), AIR);
        assertEquals(0, struct.getBlockCount());
    }

    @ParameterizedTest
    @MethodSource("structures")
    void getBlockCount_setAirWasAir_noop(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);

        struct.setBlockState(new KibuBlockPos(1, 1, 1), AIR);
        assertEquals(0, struct.getBlockCount());
    }

    @ParameterizedTest
    @MethodSource("structures")
    void isEmpty_empty_true(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);
        assertTrue(struct.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("structures")
    void isEmpty_notEmpty_false(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);
        struct.setBlockState(new KibuBlockPos(1), new BuiltinKibuBlockState("foo"));
        assertFalse(struct.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("structures")
    void addEntity_notContained_returnsTrue(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);

        var entity = TestKibuEntity.of("test", 1.0, 0.0, 0.5);
        assertTrue(struct.addEntity(entity));
    }

    @ParameterizedTest
    @MethodSource("structures")
    void addEntity_contained_returnsFalse(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);

        var entity = TestKibuEntity.of("test", 1.0, 0.0, 0.5);
        struct.addEntity(entity);
        assertFalse(struct.addEntity(entity));
    }

    @ParameterizedTest
    @MethodSource("structures")
    void getEntities_initial_empty(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);
        assertTrue(struct.getEntities().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("structures")
    void getEntities_added_returned(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);

        var entity = TestKibuEntity.of("test", 1.0, 0.0, 0.5);
        var entity2 = TestKibuEntity.of("foo", 1.0, 0.0, 0.5);

        struct.addEntity(entity);
        struct.addEntity(entity2);

        assertEquals(Set.of(entity, entity2), new HashSet<>(struct.getEntities()));
    }

    @ParameterizedTest
    @MethodSource("structures")
    void getEntities_mutability_isImmutable(BlockStructureFactory factory) {
        var struct = factory.create(2, 2, 2, new KibuBlockPos(1, 1, 1), 0);

        var entity = TestKibuEntity.of("test", 1.0, 0.0, 0.5);
        var entity2 = TestKibuEntity.of("foo", 1.0, 0.0, 0.5);

        struct.addEntity(entity);
        struct.addEntity(entity2);

        assertThrows(RuntimeException.class, () -> struct.getEntities().remove(entity));
    }

    private static Stream<BlockStructureFactory> structures() {
        return Stream.of(
                (width, height, length, origin, dataVersion) -> new SimpleBlockStructure(dataVersion),
                ArrayBlockStructure::new
        );
    }
}
