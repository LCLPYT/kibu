package work.lclpnet.kibu.schematic.sponge;

import work.lclpnet.kibu.mc.BlockState;
import work.lclpnet.kibu.mc.BlockStateAdapter;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class TestBlockAdapter implements BlockStateAdapter {

    private final Map<String, BlockState> records = new HashMap<>();

    @Nullable
    @Override
    public BlockState getBlockState(String string) {
        return records.computeIfAbsent(string, TestBlockState::new);
    }
}
