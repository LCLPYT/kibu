package work.lclpnet.kibu.schematic;

import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.block.Blocks;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.mc.BlockState;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FabricBlockStateAdapterTest {

    @BeforeAll
    public static void setup() {
        SharedConstants.createGameVersion();
        Bootstrap.initialize();
    }

    @Test
    void testAdapt() {
        var adapter = FabricBlockStateAdapter.getInstance();

        BlockState state = adapter.getBlockState("minecraft:dirt");
        assertTrue(state instanceof FabricBlockState);
        assertEquals(Blocks.DIRT.getDefaultState(), ((FabricBlockState) state).getState());
    }

    @Test
    void testRevert() {
        var adapter = FabricBlockStateAdapter.getInstance();
        assertEquals(Blocks.DIRT.getDefaultState(), adapter.revert(new FabricBlockState(Blocks.DIRT.getDefaultState())));
    }
}