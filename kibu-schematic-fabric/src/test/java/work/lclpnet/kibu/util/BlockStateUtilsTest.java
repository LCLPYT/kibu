package work.lclpnet.kibu.util;

import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BlockStateUtilsTest {

    @BeforeAll
    public static void setup() {
        SharedConstants.createGameVersion();
        Bootstrap.initialize();
    }

    @Test
    void stringify() {
        BlockState state = Blocks.DIAMOND_BLOCK.getDefaultState();

        assertEquals("minecraft:diamond_block", BlockStateUtils.stringify(state));
    }

    @Test
    void stringify_properties() {
        BlockState state = Blocks.OAK_LEAVES.getDefaultState()
                .with(LeavesBlock.PERSISTENT, true)
                .with(LeavesBlock.WATERLOGGED, false)
                .with(LeavesBlock.DISTANCE, 5);

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

        assertEquals(Blocks.DIAMOND_BLOCK.getDefaultState(), parsed);
    }

    @Test
    void parse_properties() {
        BlockState parsed = BlockStateUtils.parse("minecraft:oak_leaves[persistent=true,distance=5]");

        assertEquals(Blocks.OAK_LEAVES.getDefaultState()
                .with(LeavesBlock.PERSISTENT, true)
                .with(LeavesBlock.DISTANCE, 5), parsed);
    }
}