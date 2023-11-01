package work.lclpnet.kibu.schematic;

import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.block.Blocks;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.mc.KibuBlockState;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FabricKibuBlockStateAdapterTest {

    @BeforeAll
    public static void setup() {
        SharedConstants.createGameVersion();
        Bootstrap.initialize();
    }

    @Test
    void testAdapt() {
        var adapter = FabricBlockStateAdapter.getInstance();

        KibuBlockState state = adapter.getBlockState("minecraft:dirt");
        assertTrue(state instanceof FabricKibuBlockState);
        assertEquals(Blocks.DIRT.getDefaultState(), ((FabricKibuBlockState) state).getState());
    }

    @Test
    void testRevert() {
        var adapter = FabricBlockStateAdapter.getInstance();
        assertEquals(Blocks.DIRT.getDefaultState(), adapter.revert(new FabricKibuBlockState(Blocks.DIRT.getDefaultState())));
    }
}