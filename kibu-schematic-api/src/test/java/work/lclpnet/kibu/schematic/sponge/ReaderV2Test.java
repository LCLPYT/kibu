package work.lclpnet.kibu.schematic.sponge;

import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.mc.TestBlockAdapter;
import work.lclpnet.kibu.structure.BlockStructure;
import work.lclpnet.kibu.structure.SimpleBlockStructure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
