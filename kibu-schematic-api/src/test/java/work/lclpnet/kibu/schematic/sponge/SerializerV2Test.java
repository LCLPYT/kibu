package work.lclpnet.kibu.schematic.sponge;

import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.mc.BlockPos;
import work.lclpnet.kibu.mc.BuiltinBlockState;
import work.lclpnet.kibu.structure.SimpleBlockStructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SerializerV2Test {

    @Test
    public void testWrite() {
        var structure = new SimpleBlockStructure(123);
        structure.setBlockState(new BlockPos(0, 0, 0), new BuiltinBlockState("minecraft:stone"));
        structure.setBlockState(new BlockPos(0, 1, 0), new BuiltinBlockState("minecraft:dirt"));

        var serializer = new SerializerV2();
        var nbt = serializer.serialize(structure);

        assertNotNull(nbt);
        assertEquals(10, nbt.keySet().size());
    }

    @Test
    public void testWriteWithAir() {
        var structure = new SimpleBlockStructure(123);
        structure.setBlockState(new BlockPos(0, 0, 0), new BuiltinBlockState("minecraft:stone"));
        structure.setBlockState(new BlockPos(0, 1, 0), new BuiltinBlockState("minecraft:dirt"));
        structure.setBlockState(new BlockPos(1, 1, 0), new BuiltinBlockState("minecraft:grass_block"));
        // bounds are now [0,0,0] to [1, 1, 0]. block at [1, 0, 0] is air

        var serializer = new SerializerV2();
        var nbt = serializer.serialize(structure);

        assertNotNull(nbt);
        assertEquals(10, nbt.keySet().size());
        assertEquals(4, nbt.getCompound("Palette").keySet().size());
    }
}