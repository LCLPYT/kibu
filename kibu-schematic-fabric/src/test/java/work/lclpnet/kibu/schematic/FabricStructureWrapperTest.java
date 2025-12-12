package work.lclpnet.kibu.schematic;

import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.block.Blocks;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class FabricStructureWrapperTest {

    @BeforeAll
    public static void setup() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    void testBlockView() {
        var struct = new FabricStructureWrapper();
        struct.setBlockState(new BlockPos(2, 3, 1), Blocks.OBSIDIAN.defaultBlockState());
        struct.setBlockState(new BlockPos(0, 0, 0), Blocks.STONE.defaultBlockState());

        assertNotNull(struct.getStructure());
        assertEquals(4, struct.getHeight());
        assertEquals(0, struct.getMinY());
        assertNull(struct.getBlockEntity(new BlockPos(0, 0, 0)));
    }

    @Test
    void testCopyTo() {
        var struct = new FabricStructureWrapper();
        struct.setBlockState(new BlockPos(2, 3, 1), Blocks.OBSIDIAN.defaultBlockState());
        struct.setBlockState(new BlockPos(0, 0, 0), Blocks.STONE.defaultBlockState());

        assertEquals(2, StreamSupport.stream(struct.getBlockPositions().spliterator(), false).count());

        var other = new FabricStructureWrapper();
        struct.copyTo(other);

        assertEquals(2, StreamSupport.stream(other.getBlockPositions().spliterator(), false).count());
    }
}