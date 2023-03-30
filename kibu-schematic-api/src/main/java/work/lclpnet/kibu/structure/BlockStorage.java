package work.lclpnet.kibu.structure;

import work.lclpnet.kibu.mc.BlockPos;
import work.lclpnet.kibu.mc.BlockState;

public interface BlockStorage {

    void setBlockState(BlockPos pos, BlockState state);

    BlockState getBlockState(BlockPos pos);

    Iterable<BlockPos> getBlockPositions();
}
