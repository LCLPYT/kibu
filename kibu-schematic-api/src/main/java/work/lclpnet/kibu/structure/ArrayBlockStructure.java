package work.lclpnet.kibu.structure;

import work.lclpnet.kibu.mc.BuiltinKibuBlockState;
import work.lclpnet.kibu.mc.KibuBlockEntity;
import work.lclpnet.kibu.mc.KibuBlockPos;
import work.lclpnet.kibu.mc.KibuBlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Iterator;

public class ArrayBlockStructure implements BlockStructure {

    private final KibuBlockState[][][] states;
    private final int width, height, length;
    private final KibuBlockPos origin;
    private final int dataVersion;
    private int blockCount = 0;

    public ArrayBlockStructure(int width, int height, int length, KibuBlockPos origin, int dataVersion) {
        this.width = width;
        this.height = height;
        this.length = length;
        this.origin = origin;
        this.dataVersion = dataVersion;

        this.states = new KibuBlockState[height][width][length];

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

    @Nullable
    @Override
    public KibuBlockEntity getBlockEntity(KibuBlockPos pos) {
        return null;
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

        KibuBlockState oldState = states[y][x][z];
        boolean wasEmpty = oldState == null || oldState.isAir();

        states[y][x][z] = state;

        boolean isEmpty = state == null || state.isAir();

        if (wasEmpty && !isEmpty) {
            blockCount++;
        } else if (!wasEmpty && isEmpty) {
            blockCount--;
        }
    }

    @Nonnull
    @Override
    public KibuBlockState getBlockState(KibuBlockPos pos) {
        int x = pos.getX() - origin.getX(), y = pos.getY() - origin.getY(), z = pos.getZ() - origin.getZ();

        if (outside(x, y, z)) return BuiltinKibuBlockState.AIR;

        KibuBlockState state = states[y][x][z];

        return state != null ? state : BuiltinKibuBlockState.AIR;
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

    public boolean outside(int rx, int ry, int rz) {
        return rx < 0 || rz < 0 || rx >= width || rz >= length || ry < 0 || ry >= height;
    }
}
