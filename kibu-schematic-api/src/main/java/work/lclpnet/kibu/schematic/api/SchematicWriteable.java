package work.lclpnet.kibu.schematic.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.mc.KibuBlockEntity;
import work.lclpnet.kibu.mc.KibuBlockPos;
import work.lclpnet.kibu.mc.KibuBlockState;
import work.lclpnet.kibu.mc.KibuEntity;

import java.util.Collection;

public interface SchematicWriteable extends Cuboid {

    @NotNull
    KibuBlockState getBlockState(KibuBlockPos pos);

    @Nullable
    KibuBlockEntity getBlockEntity(KibuBlockPos pos);

    Collection<? extends KibuEntity> getEntities();

    int getDataVersion();
}
