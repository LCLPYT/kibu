package work.lclpnet.kibu.schematic.sponge;

import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.mc.TestBlockAdapter;
import work.lclpnet.kibu.structure.BlockStructure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
            structure = reader.read(in, adapter);
        }

        assertNotNull(structure);
        assertEquals(3337, structure.getDataVersion());
    }
}
