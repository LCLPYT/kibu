package work.lclpnet.kibu.schematic.io;

import java.util.Iterator;
import java.util.Objects;

public class VarIntReader implements Iterator<Integer> {

    private final byte[] bytes;
    private int index = 0;

    public VarIntReader(byte[] bytes) {
        this.bytes = Objects.requireNonNull(bytes);
    }

    @Override
    public boolean hasNext() {
        return index < bytes.length;
    }

    @Override
    public Integer next() {
        // read next var int
        int varInt = 0;
        int varIntPos = 0;

        while (index < bytes.length) {
            varInt |= (bytes[index] & 127) << (varIntPos++ * 7);

            if (varIntPos > 5) throw new IllegalStateException("var int out of bounds");

            if ((bytes[index] & 128) != 128) {
                index++;
                return varInt;
            }

            index++;
        }

        throw new IllegalStateException("Incomplete VarInt (end of data)");
    }
}
