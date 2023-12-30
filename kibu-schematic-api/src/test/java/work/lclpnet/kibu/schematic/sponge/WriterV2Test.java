package work.lclpnet.kibu.schematic.sponge;

import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.jnbt.io.NbtIOHelper;
import work.lclpnet.kibu.structure.SimpleBlockStructure;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WriterV2Test {

    @Test
    public void testWrite() throws IOException {
        var structure = new SimpleBlockStructure(123);
        var serializer = new SerializerV2();
        var writer = new WriterV2(serializer);

        var bytes = writer.toArray(structure);
        var nbt = NbtIOHelper.fromArray(bytes);
        assertEquals(SpongeSchematicV2.SCHEMATIC, nbt.name());

        var tag = nbt.tag();
        assertTrue(tag instanceof CompoundTag);
        assertEquals(11, ((CompoundTag) tag).keySet().size());
    }
}
