package work.lclpnet.kibu.structure;

import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.mc.KibuBlockEntity;
import work.lclpnet.kibu.mc.KibuBlockPos;

public interface BlockEntityStorage {

    @Nullable
    KibuBlockEntity getBlockEntity(KibuBlockPos pos);

    void setBlockEntity(KibuBlockPos pos, KibuBlockEntity blockEntity);

    int getBlockEntityCount();
}
