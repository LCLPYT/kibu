package work.lclpnet.kibu.structure;

import work.lclpnet.kibu.mc.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ArrayBlockStructure implements BlockStructure {

    private final Entry[][][] states;
    private final int width, height, length;
    private final KibuBlockPos origin;
    private final int dataVersion;
    private final Collection<KibuEntity> entities = new HashSet<>();
    private int blockCount = 0, blockEntityCount = 0;

    public ArrayBlockStructure(int width, int height, int length, KibuBlockPos origin, int dataVersion) {
        this.width = width;
        this.height = height;
        this.length = length;
        this.origin = origin;
        this.dataVersion = dataVersion;

        this.states = new Entry[height][width][length];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Arrays.fill(states[y][x], null);
            }
        }
    }

    @Override
    public KibuBlockPos getOrigin() {
        return origin;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public int getDataVersion() {
        return dataVersion;
    }

    @Override
    public void setBlockState(KibuBlockPos pos, KibuBlockState state) {
        int x = pos.getX() - origin.getX(), y = pos.getY() - origin.getY(), z = pos.getZ() - origin.getZ();

        if (outside(x, y, z))
            throw new UnsupportedOperationException("Block out of structure bounds");

        Entry oldEntry = states[y][x][z];
        boolean wasEmpty = oldEntry == null || oldEntry.state.isAir();
        boolean isEmpty = state == null || state.isAir();

        if (wasEmpty && !isEmpty) {
            states[y][x][z] = new Entry(state);
            blockCount++;
        } else if (!wasEmpty && isEmpty) {
            if (states[y][x][z].blockEntity != null) {
                blockEntityCount--;
            }

            states[y][x][z] = null;
            blockCount--;
        } else if (!isEmpty) {
            boolean sameState = oldEntry.state.getAsString().equals(state.getAsString());

            // reset block entity if blocks mismatch
            if (!sameState && states[y][x][z].blockEntity != null) {
                states[y][x][z].blockEntity = null;
                blockEntityCount--;
            }
        }
    }

    @Nonnull
    @Override
    public KibuBlockState getBlockState(KibuBlockPos pos) {
        Entry entry = getEntry(pos);

        return entry != null ? entry.state : BuiltinKibuBlockState.AIR;
    }

    @Override
    public Iterable<KibuBlockPos> getBlockPositions() {
        return this::iterator;
    }

    public Iterator<KibuBlockPos> iterator() {
        KibuBlockPos min = new KibuBlockPos(0, 0, 0);
        KibuBlockPos max = new KibuBlockPos(width - 1, height - 1, length - 1);

        var parent = KibuBlockPos.cuboidIterator(min, max);

        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return parent.hasNext();
            }

            @Override
            public KibuBlockPos next() {
                return parent.next().add(origin);
            }
        };
    }

    @Override
    public int getBlockCount() {
        return blockCount;
    }

    @Nullable
    @Override
    public KibuBlockEntity getBlockEntity(KibuBlockPos pos) {
        Entry entry = getEntry(pos);

        if (entry == null) return null;

        return entry.blockEntity;
    }

    @Override
    public void setBlockEntity(KibuBlockPos pos, KibuBlockEntity blockEntity) {
        Entry entry = getEntry(pos);

        if (entry == null || entry.state.isAir()) return;  // block state must be stored first

        boolean hadBlockEntity = entry.blockEntity != null;
        boolean hasBlockEntity = blockEntity != null;

        entry.blockEntity = blockEntity;

        if (!hadBlockEntity && hasBlockEntity) {
            blockEntityCount++;
        } else if (hadBlockEntity && !hasBlockEntity) {
            blockEntityCount--;
        }
    }

    @Override
    public int getBlockEntityCount() {
        return blockEntityCount;
    }

    @Nullable
    private Entry getEntry(KibuBlockPos pos) {
        int x = pos.getX() - origin.getX(), y = pos.getY() - origin.getY(), z = pos.getZ() - origin.getZ();

        if (outside(x, y, z)) {
            return null;
        }

        return states[y][x][z];
    }

    public boolean outside(int rx, int ry, int rz) {
        return rx < 0 || rz < 0 || rx >= width || rz >= length || ry < 0 || ry >= height;
    }

    @Override
    public boolean addEntity(KibuEntity entity) {
        return entities.add(entity);
    }

    @Override
    public boolean removeEntity(KibuEntity entity) {
        return entities.remove(entity);
    }

    @Override
    public Collection<? extends KibuEntity> getEntities() {
        return Collections.unmodifiableCollection(entities);
    }

    private static class Entry {
        @Nonnull KibuBlockState state;
        @Nullable
        KibuBlockEntity blockEntity = null;

        Entry(KibuBlockState state) {
            this.state = state;
        }
    }
}
