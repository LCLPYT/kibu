package work.lclpnet.kibu.schematic.sponge;

import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.jnbt.NBTConstants;
import work.lclpnet.kibu.jnbt.TagInfo;
import work.lclpnet.kibu.jnbt.io.NbtIOHelper;
import work.lclpnet.kibu.mc.TestBlockAdapter;
import work.lclpnet.kibu.structure.SimpleBlockStructure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class DeserializerV2Test {

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

        CompoundTag nbt = (CompoundTag) tag;
        assertEquals(11, nbt.keySet().size());

        var deserializer = new DeserializerV2();
        var schematic = deserializer.deserialize(nbt, new TestBlockAdapter(), (width, height, length, origin, dataVersion) -> new SimpleBlockStructure(dataVersion));

        assertNotNull(schematic);
        // schematic is padded with air, thus the structure dimension will not be equal to the schematic dimensions
        // this depends on the BlockStructure implementation; here SimpleBlockStructure is used
        assertEquals(5, schematic.getWidth());
        assertEquals(9, schematic.getHeight());
        assertEquals(3, schematic.getLength());
    }

    @Test
    public void testRead_ArrayBlockStructure() throws IOException {
        var testSchematic = Path.of("src/test/resources/test.schem");

        TagInfo tagInfo;
        try (var in = Files.newInputStream(testSchematic)) {
            tagInfo = NbtIOHelper.read(in);
        }

        assertEquals("Schematic", tagInfo.name());

        var tag = tagInfo.tag();
        assertEquals(NBTConstants.TYPE_COMPOUND, tag.getType());
        assertTrue(tag instanceof CompoundTag);

        CompoundTag nbt = (CompoundTag) tag;
        assertEquals(11, nbt.keySet().size());

        var deserializer = new DeserializerV2();
        var schematic = deserializer.deserialize(nbt, new TestBlockAdapter());

        assertNotNull(schematic);
        assertEquals(9, schematic.getWidth());
        assertEquals(11, schematic.getHeight());
        assertEquals(7, schematic.getLength());
    }
}