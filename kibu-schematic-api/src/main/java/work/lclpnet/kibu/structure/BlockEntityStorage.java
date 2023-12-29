package work.lclpnet.kibu.structure;

import work.lclpnet.kibu.mc.KibuBlockEntity;
import work.lclpnet.kibu.mc.KibuBlockPos;

import javax.annotation.Nullable;

public interface BlockEntityStorage {

    @Nullable
    KibuBlockEntity getBlockEntity(KibuBlockPos pos);

    void setBlockEntity(KibuBlockPos pos, KibuBlockEntity blockEntity);

    int getBlockEntityCount();
}
