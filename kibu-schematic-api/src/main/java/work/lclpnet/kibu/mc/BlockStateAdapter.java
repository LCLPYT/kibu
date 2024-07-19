package work.lclpnet.kibu.mc;

import org.jetbrains.annotations.Nullable;

public interface BlockStateAdapter {

    @Nullable
    KibuBlockState getBlockState(String string);
}
