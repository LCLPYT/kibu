package work.lclpnet.kibu.schematic.api;

import work.lclpnet.kibu.mc.BlockEntity;
import work.lclpnet.kibu.mc.BlockPos;
import work.lclpnet.kibu.mc.BlockState;

public interface SchematicWriteable extends Cuboid {

    BlockState getBlockState(BlockPos pos);

    BlockEntity getBlockEntity(BlockPos pos);

    int getDataVersion();
}
