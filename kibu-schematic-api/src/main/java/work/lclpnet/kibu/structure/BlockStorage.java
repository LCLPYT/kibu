package work.lclpnet.kibu.structure;

import work.lclpnet.kibu.mc.KibuBlockPos;
import work.lclpnet.kibu.mc.KibuBlockState;

public interface BlockStorage {

    void setBlockState(KibuBlockPos pos, KibuBlockState state);

    KibuBlockState getBlockState(KibuBlockPos pos);

    Iterable<KibuBlockPos> getBlockPositions();

    int getBlockCount();

    default boolean isEmpty() {
        return getBlockCount() <= 0;
    }
}
