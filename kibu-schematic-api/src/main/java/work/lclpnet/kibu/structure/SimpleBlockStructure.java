package work.lclpnet.kibu.structure;

import work.lclpnet.kibu.mc.BuiltinKibuBlockState;
import work.lclpnet.kibu.mc.KibuBlockEntity;
import work.lclpnet.kibu.mc.KibuBlockPos;
import work.lclpnet.kibu.mc.KibuBlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class SimpleBlockStructure implements BlockStructure {

    private final Map<KibuBlockPos, KibuBlockState> blocks = new HashMap<>();
    private final Map<KibuBlockPos, KibuBlockEntity> blockEntities = new HashMap<>();
    private final int dataVersion;
    private final transient KibuBlockPos.Mutable minPos = new KibuBlockPos.Mutable(Integer.MAX_VALUE);
    private final transient KibuBlockPos.Mutable maxPos = new KibuBlockPos.Mutable(Integer.MIN_VALUE);

    public SimpleBlockStructure(int dataVersion) {
        this.dataVersion = dataVersion;
    }

    @Override
    public void setBlockState(KibuBlockPos pos, @Nullable KibuBlockState state) {
        synchronized (blocks) {
            if (state == null || state.isAir()) {
                if (blocks.remove(pos) == null) return;  // nothing removed

                // remove block entity, if it exists
                blockEntities.remove(pos);

                // check if on the surface => this position is critical to the bounds
                if (!isOnSurfaceInternal(pos)) return;

                // recalculate bounds, because the position was on the border (surface of the cube)
                minPos.set(Integer.MAX_VALUE);
                maxPos.set(Integer.MIN_VALUE);

                getBlockPositions().forEach(this::updateBounds);

                return;
            }

            // state is non-null here
            KibuBlockState old = blocks.put(pos.toImmutable(), state);

            // reset block entity if blocks mismatch
            if (old == null || !old.getAsString().equals(state.getAsString())) {
                blockEntities.remove(pos);
            }

            updateBounds(pos);
        }
    }

    private void updateBounds(KibuBlockPos pos) {
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
    public KibuBlockState getBlockState(KibuBlockPos pos) {
        if (!isWithinBox(pos)) {
            return BuiltinKibuBlockState.AIR;
        }

        KibuBlockState kibuBlockState;

        synchronized (blocks) {
            kibuBlockState = blocks.get(pos);
        }

        return kibuBlockState == null ? BuiltinKibuBlockState.AIR : kibuBlockState;
    }

    @Override
    public int getDataVersion() {
        return dataVersion;
    }

    @Override
    public Iterable<KibuBlockPos> getBlockPositions() {
        Iterable<KibuBlockPos> copy;

        synchronized (blocks) {
            copy = new HashSet<>(blocks.keySet());
        }

        return copy;
    }

    @Override
    public int getBlockCount() {
        synchronized (blocks) {
            return blocks.size();
        }
    }

    @Override
    public KibuBlockPos getOrigin() {
        synchronized (blocks) {
            if (blocks.isEmpty()) return new KibuBlockPos(0);
            return new KibuBlockPos(minPos);
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
        return Objects.equals(blocks, that.blocks) && Objects.equals(blockEntities, that.blockEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blocks, blockEntities);
    }

    @Override
    public @Nullable KibuBlockEntity getBlockEntity(KibuBlockPos pos) {
        return blockEntities.get(pos);
    }

    @Override
    public void setBlockEntity(KibuBlockPos pos, KibuBlockEntity blockEntity) {
        if (blockEntity == null) {
            blockEntities.remove(pos);
            return;
        }

        KibuBlockState state = getBlockState(pos);

        if (state.isAir()) return;  // block state must be stored first

        blockEntities.put(pos.toImmutable(), blockEntity);
    }

    @Override
    public int getBlockEntityCount() {
        return blockEntities.size();
    }

    public boolean isWithinBox(KibuBlockPos pos) {
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

    private boolean isOnSurfaceInternal(KibuBlockPos pos) {
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

    public boolean isOnSurface(KibuBlockPos pos) {
        synchronized (blocks) {
            return isOnSurfaceInternal(pos);
        }
    }
}
