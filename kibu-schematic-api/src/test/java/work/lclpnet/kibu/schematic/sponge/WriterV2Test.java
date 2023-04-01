package work.lclpnet.kibu.schematic.sponge;

import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.mc.BlockPos;
import work.lclpnet.kibu.structure.SimpleBlockStructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WriterV2Test {

    @Test
    public void testWrite() {
        var structure = new SimpleBlockStructure(123);
        structure.setBlockState(new BlockPos(0, 0, 0), new TestBlockState("minecraft:stone"));
        structure.setBlockState(new BlockPos(0, 1, 0), new TestBlockState("minecraft:dirt"));

        var writer = new WriterV2();
        var nbt = writer.write(structure);

        assertNotNull(nbt);
        assertEquals(1, nbt.keySet().size());

        var schematic = nbt.getCompound("Schematic");
        assertEquals(10, schematic.keySet().size());
    }

    @Test
    public void testWriteWithAir() {
        var structure = new SimpleBlockStructure(123);
        structure.setBlockState(new BlockPos(0, 0, 0), new TestBlockState("minecraft:stone"));
        structure.setBlockState(new BlockPos(0, 1, 0), new TestBlockState("minecraft:dirt"));
        structure.setBlockState(new BlockPos(1, 1, 0), new TestBlockState("minecraft:grass_block"));
        // bounds are now [0,0,0] to [1, 1, 0]. block at [1, 0, 0] is air

        var writer = new WriterV2();
        var nbt = writer.write(structure);

        assertNotNull(nbt);
        assertEquals(1, nbt.keySet().size());

        var schematic = nbt.getCompound("Schematic");
        assertEquals(10, schematic.keySet().size());
        assertEquals(4, schematic.getCompound("Palette").keySet().size());
    }
}