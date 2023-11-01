package work.lclpnet.kibu.mc;

import javax.annotation.Nullable;

public interface BlockStateAdapter {

    @Nullable
    KibuBlockState getBlockState(String string);
}
