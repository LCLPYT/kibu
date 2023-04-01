package work.lclpnet.kibu.structure;

import work.lclpnet.kibu.mc.BlockEntity;
import work.lclpnet.kibu.mc.BlockPos;
import work.lclpnet.kibu.mc.BlockState;
import work.lclpnet.kibu.mc.BuiltinBlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class SimpleBlockStructure implements BlockStructure {

    private final Map<BlockPos, BlockState> blocks = new HashMap<>();
    private final BlockPos minPos = new BlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    private final BlockPos maxPos = new BlockPos(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    private final int dataVersion;

    public SimpleBlockStructure(int dataVersion) {
        this.dataVersion = dataVersion;
    }

    @Override
    public void setBlockState(BlockPos pos, @Nullable BlockState state) {
        synchronized (blocks) {
            if (state == null || state.isAir()) {
                blocks.remove(pos);
            } else {
                blocks.put(pos, state);
            }

            // TODO consider removal which will shrink the structure
            minPos.set(
                    Math.min(minPos.getX(), pos.getX()),
                    Math.min(minPos.getY(), pos.getY()),
                    Math.min(minPos.getZ(), pos.getZ())
            );

            maxPos.set(
                    Math.max(maxPos.getX(), pos.getX()),
                    Math.max(maxPos.getY(), pos.getY()),
                    Math.max(maxPos.getZ(), pos.getZ())
            );
        }
    }

    @Nonnull
    @Override
    public BlockState getBlockState(BlockPos pos) {
        BlockState blockState;

        synchronized (blocks) {
            blockState = blocks.get(pos);
        }

        return blockState == null ? BuiltinBlockState.AIR : blockState;
    }

    @Override
    public @Nullable BlockEntity getBlockEntity(BlockPos pos) {
        return null;  // block entities are not supported yet
    }

    @Override
    public int getDataVersion() {
        return dataVersion;
    }

    @Override
    public Iterable<BlockPos> getBlockPositions() {
        Iterable<BlockPos> copy;

        synchronized (blocks) {
            copy = new HashSet<>(blocks.keySet());
        }

        return copy;
    }

    @Override
    public BlockPos getOffset() {
        return minPos;
    }

    @Override
    public int getWidth() {
        return maxPos.getX() - minPos.getX() + 1;
    }

    @Override
    public int getHeight() {
        return maxPos.getY() - minPos.getY() + 1;
    }

    @Override
    public int getLength() {
        return maxPos.getZ() - minPos.getZ() + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleBlockStructure that = (SimpleBlockStructure) o;
        return blocks.equals(that.blocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blocks);
    }
}
