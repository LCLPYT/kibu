package work.lclpnet.kibu.schematic.api;

import work.lclpnet.kibu.mc.BlockEntity;
import work.lclpnet.kibu.mc.BlockPos;
import work.lclpnet.kibu.mc.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface SchematicWriteable extends Cuboid {

    @Nonnull
    BlockState getBlockState(BlockPos pos);

    @Nullable
    BlockEntity getBlockEntity(BlockPos pos);

    int getDataVersion();
}
