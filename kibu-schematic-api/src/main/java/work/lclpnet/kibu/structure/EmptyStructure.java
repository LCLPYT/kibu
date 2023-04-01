package work.lclpnet.kibu.structure;

import work.lclpnet.kibu.mc.BlockEntity;
import work.lclpnet.kibu.mc.BlockPos;
import work.lclpnet.kibu.mc.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

class EmptyStructure implements BlockStructure {

    private final List<BlockPos> positions = Collections.emptyList();
    private final BlockPos pos = new BlockPos(0, 0, 0);

    @Override
    public @Nullable BlockEntity getBlockEntity(BlockPos pos) {
        return null;
    }

    @Override
    public int getDataVersion() {
        return 0;
    }

    @Override
    public BlockPos getOffset() {
        return pos;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public void setBlockState(BlockPos pos, BlockState state) {
    }

    @Nonnull
    @Override
    public BlockState getBlockState(BlockPos pos) {
        return null;
    }

    @Override
    public Iterable<BlockPos> getBlockPositions() {
        return positions;
    }
}
