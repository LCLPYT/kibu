package work.lclpnet.kibu.schematic.api;

import work.lclpnet.kibu.mc.KibuBlockEntity;
import work.lclpnet.kibu.mc.KibuBlockPos;
import work.lclpnet.kibu.mc.KibuBlockState;
import work.lclpnet.kibu.mc.KibuEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public interface SchematicWriteable extends Cuboid {

    @Nonnull
    KibuBlockState getBlockState(KibuBlockPos pos);

    @Nullable
    KibuBlockEntity getBlockEntity(KibuBlockPos pos);

    Collection<? extends KibuEntity> getEntities();

    int getDataVersion();
}
