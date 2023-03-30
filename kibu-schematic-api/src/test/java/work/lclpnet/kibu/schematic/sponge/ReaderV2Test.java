package work.lclpnet.kibu.schematic.sponge;

import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.jnbt.NBTConstants;
import work.lclpnet.kibu.jnbt.TagInfo;
import work.lclpnet.kibu.jnbt.io.NbtIOHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ReaderV2Test {

    @Test
    public void testRead() throws IOException {
        var testSchematic = Path.of("src/test/resources/test.schem");

        TagInfo tagInfo;
        try (var in = Files.newInputStream(testSchematic)) {
            tagInfo = NbtIOHelper.read(in);
        }

        assertEquals("Schematic", tagInfo.name());

        var tag = tagInfo.tag();
        assertEquals(NBTConstants.TYPE_COMPOUND, tag.getType());
        assertTrue(tag instanceof CompoundTag);

        assertEquals(11, ((CompoundTag) tag).keySet().size());

        var nbt = tagInfo.rootTag();

        var reader = new ReaderV2();
        var schematic = reader.read(nbt, new TestBlockAdapter());

        assertNotNull(schematic);
        assertEquals(9, schematic.getWidth());
        assertEquals(7, schematic.getLength());
        assertEquals(11, schematic.getHeight());
    }
}