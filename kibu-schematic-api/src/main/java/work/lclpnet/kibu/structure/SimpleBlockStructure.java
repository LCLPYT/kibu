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
    private final int dataVersion;
    private final transient BlockPos.Mutable minPos = new BlockPos.Mutable(Integer.MAX_VALUE);
    private final transient BlockPos.Mutable maxPos = new BlockPos.Mutable(Integer.MIN_VALUE);

    public SimpleBlockStructure(int dataVersion) {
        this.dataVersion = dataVersion;
    }

    @Override
    public void setBlockState(BlockPos pos, @Nullable BlockState state) {
        synchronized (blocks) {
            if (state == null || state.isAir()) {
                if (blocks.remove(pos) == null) return;  // nothing removed

                // check if on the surface => this position is critical to the bounds
                if (!isOnSurfaceInternal(pos)) return;

                // recalculate bounds, because the position was on the border (surface of the cube)
                minPos.set(Integer.MAX_VALUE);
                maxPos.set(Integer.MIN_VALUE);

                getBlockPositions().forEach(this::updateBounds);

                return;
            }

            blocks.put(new BlockPos(pos), state);

            updateBounds(pos);
        }
    }

    private void updateBounds(BlockPos pos) {
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

    @Nonnull
    @Override
    public BlockState getBlockState(BlockPos pos) {
        if (!isWithinBox(pos)) {
            return BuiltinBlockState.AIR;
        }

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
    public BlockPos getOrigin() {
        synchronized (blocks) {
            if (blocks.isEmpty()) return new BlockPos(0);
            return new BlockPos(minPos);
        }
    }

    @Override
    public int getWidth() {
        synchronized (blocks) {
            if (maxPos.getX() < minPos.getX()) return 0;
            return maxPos.getX() - minPos.getX() + 1;
        }
    }

    @Override
    public int getHeight() {
        synchronized (blocks) {
            if (maxPos.getY() < minPos.getY()) return 0;
            return maxPos.getY() - minPos.getY() + 1;
        }
    }

    @Override
    public int getLength() {
        synchronized (blocks) {
            if (maxPos.getZ() < minPos.getZ()) return 0;
            return maxPos.getZ() - minPos.getZ() + 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleBlockStructure that = (SimpleBlockStructure) o;

        Map<BlockPos, BlockState> thatBlocks;
        synchronized (that.blocks) {
            thatBlocks = that.blocks;
        }

        synchronized (blocks) {
            return blocks.equals(thatBlocks);
        }
    }

    @Override
    public int hashCode() {
        synchronized (blocks) {
            return Objects.hash(blocks);
        }
    }

    public boolean isWithinBox(BlockPos pos) {
        final int x = pos.getX();
        final int y = pos.getY();
        final int z = pos.getZ();

        synchronized (blocks) {
            if ((minPos.getX() > maxPos.getX()) || (minPos.getY() > maxPos.getY()) || (minPos.getZ() > maxPos.getZ())) {
                return false;
            }

            return (x >= minPos.getX() && x <= maxPos.getX())
                    && (y >= minPos.getY() && y <= maxPos.getY())
                    && (z >= minPos.getZ() && z <= maxPos.getZ());
        }
    }

    private boolean isOnSurfaceInternal(BlockPos pos) {
        // synchronization on blocks is done by the caller
        if ((minPos.getX() > maxPos.getX()) || (minPos.getY() > maxPos.getY()) || (minPos.getZ() > maxPos.getZ())) {
            return false;
        }

        final int x = pos.getX();
        final int y = pos.getY();
        final int z = pos.getZ();

        return (x == minPos.getX() || x == maxPos.getX())
                || (y == minPos.getY() || y == maxPos.getY())
                || (z == minPos.getZ() || z == maxPos.getZ());
    }

    public boolean isOnSurface(BlockPos pos) {
        synchronized (blocks) {
            return isOnSurfaceInternal(pos);
        }
    }
}
