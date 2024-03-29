package work.lclpnet.kibu.mc;

import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class KibuBlockPos {

    private int x, y, z;

    public KibuBlockPos() {
        this(0);
    }

    public KibuBlockPos(int v) {
        this(v, v, v);
    }

    public KibuBlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public KibuBlockPos(KibuBlockPos other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public KibuBlockPos add(int x, int y, int z) {
        return new KibuBlockPos(this.x + x, this.y + y, this.z + z);
    }

    public KibuBlockPos add(KibuBlockPos pos) {
        return add(pos.x, pos.y, pos.z);
    }

    public KibuBlockPos sub(int x, int y, int z) {
        return add(-x, -y, -z);
    }

    public KibuBlockPos sub(KibuBlockPos pos) {
        return sub(pos.x, pos.y, pos.z);
    }

    public KibuBlockPos toImmutable() {
        return new KibuBlockPos(this);
    }

    @Override
    public String toString() {
        return "BlockPos{x=%d, y=%d, z=%d}".formatted(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KibuBlockPos kibuBlockPos)) return false;
        return x == kibuBlockPos.x && y == kibuBlockPos.y && z == kibuBlockPos.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public static class Mutable extends KibuBlockPos {

        public Mutable() {
            super();
        }

        public Mutable(int v) {
            super(v);
        }

        public Mutable(int x, int y, int z) {
            super(x, y, z);
        }

        public Mutable(KibuBlockPos pos) {
            super(pos);
        }

        public void set(int v) {
            set(v, v, v);
        }

        public void set(int x, int y, int z) {
            super.x = x;
            super.y = y;
            super.z = z;
        }

        @Override
        public KibuBlockPos add(int x, int y, int z) {
            super.x += x;
            super.y += y;
            super.z += z;

            return this;
        }
    }

    public static Stream<KibuBlockPos> streamCuboid(KibuBlockPos from, KibuBlockPos to) {
        return StreamSupport.stream(iterateCuboid(from, to).spliterator(), false);
    }

    public static Iterable<KibuBlockPos> iterateCuboid(KibuBlockPos from, KibuBlockPos to) {
        return () -> cuboidIterator(from, to);
    }

    public static Iterator<KibuBlockPos> cuboidIterator(KibuBlockPos from, KibuBlockPos to) {
        final var min = new KibuBlockPos(
                Math.min(from.x, to.x),
                Math.min(from.y, to.y),
                Math.min(from.z, to.z)
        );

        final var max = new KibuBlockPos(
                Math.max(from.x, to.x),
                Math.max(from.y, to.y),
                Math.max(from.z, to.z)
        );

        final int width = max.x - min.x + 1;
        final int height = max.y - min.y + 1;
        final int length = max.z - min.z + 1;

        return new CuboidPosIterator(min, width, height, length);
    }

    private static class CuboidPosIterator implements Iterator<KibuBlockPos> {

        private final KibuBlockPos offset;
        private final int width;
        private final int length;
        private final int maxIndex;
        private final Mutable pos = new KibuBlockPos.Mutable();
        private int i = 0;

        private CuboidPosIterator(KibuBlockPos offset, int width, int height, int length) {
            this.offset = Objects.requireNonNull(offset);
            this.width = width;
            this.length = length;
            this.maxIndex = width * height * length;
        }

        private void advance() {
            int y = i / (width * length);
            int x = (i % (width * length)) / length;
            int z = (i % (width * length)) % length;

            pos.set(offset.x + x, offset.y + y, offset.z + z);
        }

        @Override
        public boolean hasNext() {
            return i < maxIndex;
        }

        @Override
        public KibuBlockPos next() {
            this.advance();
            i++;

            return pos;
        }
    }
}
