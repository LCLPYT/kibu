package work.lclpnet.kibu.mc;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class TestBlockAdapter implements BlockStateAdapter {

    private final Map<String, BlockState> records = new HashMap<>();

    @Nullable
    @Override
    public BlockState getBlockState(String string) {
        return records.computeIfAbsent(string, BuiltinBlockState::new);
    }
}
