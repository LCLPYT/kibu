package work.lclpnet.kibu.util;

import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class KibuBlockStateUtilsTest {

    @BeforeAll
    public static void setup() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    void stringify() {
        BlockState state = Blocks.DIAMOND_BLOCK.defaultBlockState();

        assertEquals("minecraft:diamond_block", BlockStateUtils.stringify(state));
    }

    @Test
    void stringify_properties() {
        BlockState state = Blocks.OAK_LEAVES.defaultBlockState()
                .setValue(LeavesBlock.PERSISTENT, true)
                .setValue(LeavesBlock.WATERLOGGED, false)
                .setValue(LeavesBlock.DISTANCE, 5);

        String string = BlockStateUtils.stringify(state);
        int start = string.indexOf('[');
        assertNotEquals(-1, start);
        assertEquals(']', string.charAt(string.length() - 1));

        assertEquals("minecraft:oak_leaves", string.substring(0, start));

        String propsRaw = string.substring(start + 1, string.length() - 1);
        Set<String> props = Arrays.stream(propsRaw.split(",")).collect(Collectors.toSet());

        assertEquals(Set.of("persistent=true", "distance=5", "waterlogged=false"), props);
    }

    @Test
    void parse() {
        BlockState parsed = BlockStateUtils.parse("minecraft:diamond_block");

        assertEquals(Blocks.DIAMOND_BLOCK.defaultBlockState(), parsed);
    }

    @Test
    void parse_properties() {
        BlockState parsed = BlockStateUtils.parse("minecraft:oak_leaves[persistent=true,distance=5]");

        assertEquals(Blocks.OAK_LEAVES.defaultBlockState()
                .setValue(LeavesBlock.PERSISTENT, true)
                .setValue(LeavesBlock.DISTANCE, 5), parsed);
    }
}