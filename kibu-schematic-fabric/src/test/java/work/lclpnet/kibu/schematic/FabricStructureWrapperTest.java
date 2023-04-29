package work.lclpnet.kibu.schematic;

import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class FabricStructureWrapperTest {

    @BeforeAll
    public static void setup() {
        SharedConstants.createGameVersion();
        Bootstrap.initialize();
    }

    @Test
    void testBlockView() {
        var struct = new FabricStructureWrapper();
        struct.setBlockState(new BlockPos(2, 3, 1), Blocks.OBSIDIAN.getDefaultState());
        struct.setBlockState(new BlockPos(0, 0, 0), Blocks.STONE.getDefaultState());

        assertNotNull(struct.getStructure());
        assertEquals(4, struct.getHeight());
        assertEquals(0, struct.getBottomY());
        assertNull(struct.getBlockEntity(new BlockPos(0, 0, 0)));
    }

    @Test
    void testCopyTo() {
        var struct = new FabricStructureWrapper();
        struct.setBlockState(new BlockPos(2, 3, 1), Blocks.OBSIDIAN.getDefaultState());
        struct.setBlockState(new BlockPos(0, 0, 0), Blocks.STONE.getDefaultState());

        assertEquals(2, StreamSupport.stream(struct.getBlockPositions().spliterator(), false).count());

        var other = new FabricStructureWrapper();
        struct.copyTo(other);

        assertEquals(2, StreamSupport.stream(other.getBlockPositions().spliterator(), false).count());
    }
}