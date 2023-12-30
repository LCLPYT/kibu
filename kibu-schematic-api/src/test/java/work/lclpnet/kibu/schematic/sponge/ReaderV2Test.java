package work.lclpnet.kibu.schematic.sponge;

import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.jnbt.ListTag;
import work.lclpnet.kibu.mc.KibuBlockEntity;
import work.lclpnet.kibu.mc.KibuBlockPos;
import work.lclpnet.kibu.mc.TestBlockAdapter;
import work.lclpnet.kibu.structure.BlockStructure;
import work.lclpnet.kibu.structure.SimpleBlockStructure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

public class ReaderV2Test {

    @Test
    public void testRead() throws IOException {
        var deserializer = new DeserializerV2();
        var reader = new ReaderV2(deserializer);
        var adapter = new TestBlockAdapter();

        var testSchematic = Path.of("src/test/resources/test.schem");

        BlockStructure structure;
        try (var in = Files.newInputStream(testSchematic)) {
            structure = reader.read(in, adapter, (width, height, length, origin, dataVersion) -> new SimpleBlockStructure(dataVersion));
        }

        assertNotNull(structure);
        assertEquals(3337, structure.getDataVersion());

        // assert block positions are read correctly (dependent on default structure container -> SimpleBlockStructure)
        var positions = structure.getBlockPositions();
        assertEquals(81, StreamSupport.stream(positions.spliterator(), false).count());
    }

    @Test
    public void testRead_ArrayBlockStructure() throws IOException {
        var deserializer = new DeserializerV2();
        var reader = new ReaderV2(deserializer);
        var adapter = new TestBlockAdapter();

        var testSchematic = Path.of("src/test/resources/room.schem");

        BlockStructure structure;
        try (var in = Files.newInputStream(testSchematic)) {
            structure = reader.read(in, adapter);
        }

        assertNotNull(structure);
        assertEquals(3465, structure.getDataVersion());

        // assert block positions are read correctly (dependent on default structure container -> SimpleBlockStructure)
        var positions = structure.getBlockPositions();
        assertEquals(2025, StreamSupport.stream(positions.spliterator(), false).count());
    }

    @Test
    public void testRead_withBlockEntities() throws IOException {
        var deserializer = new DeserializerV2();
        var reader = new ReaderV2(deserializer);
        var adapter = new TestBlockAdapter();

        var testSchematic = Path.of("src/test/resources/be_e.schem");

        BlockStructure structure;
        try (var in = Files.newInputStream(testSchematic)) {
            structure = reader.read(in, adapter, (width, height, length, origin, dataVersion) -> new SimpleBlockStructure(dataVersion));
        }

        assertEquals(2, structure.getBlockEntityCount());

        KibuBlockPos pos = new KibuBlockPos(57, 64, -55);

        assertEquals("minecraft:chest[facing=south,type=right,waterlogged=false]",
                structure.getBlockState(pos).getAsString());

        KibuBlockEntity entity = structure.getBlockEntity(pos);
        assertNotNull(entity);
        assertEquals("minecraft:chest", entity.getId());

        ListTag list = entity.createNbt().getList("Items");
        assertNotNull(list, "Items is empty");
        assertEquals(8, list.getValue().size());

        entity = structure.getBlockEntity(pos.add(1, 0, 0));
        assertNotNull(entity);
        assertEquals("minecraft:chest", entity.getId());
    }

    @Test
    public void testRead_withEntities() throws IOException {
        var deserializer = new DeserializerV2();
        var reader = new ReaderV2(deserializer);
        var adapter = new TestBlockAdapter();

        var testSchematic = Path.of("src/test/resources/be_e.schem");

        BlockStructure structure;
        try (var in = Files.newInputStream(testSchematic)) {
            structure = reader.read(in, adapter, (width, height, length, origin, dataVersion) -> new SimpleBlockStructure(dataVersion));
        }

        assertEquals(2, structure.getEntities().size());

        assertTrue(structure.getEntities().stream().allMatch(entity ->
                (
                        entity.getId().equals("minecraft:painting") &&
                        isClose(entity.getX(), 60.0) &&
                        isClose(entity.getY(), 65.0) &&
                        isClose(entity.getZ(), -55.0)
                ) || (
                        entity.getId().equals("minecraft:chest_minecart") &&
                        isClose(entity.getX(), 60.5) &&
                        isClose(entity.getY(), 64.0) &&
                        isClose(entity.getZ(), -51.5)
                )));
    }

    private boolean isClose(double a, double b) {
        return Math.abs(a - b) < 10e-9;
    }
}
