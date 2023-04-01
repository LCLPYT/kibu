package work.lclpnet.kibu.mc;

import java.util.Objects;

public class BuiltinBlockState implements BlockState {

    public static final BlockState AIR = new BuiltinBlockState("minecraft:air");

    private final String blockState;

    public BuiltinBlockState(String blockState) {
        this.blockState = Objects.requireNonNull(blockState);
    }

    @Override
    public String getAsString() {
        return blockState;
    }

    @Override
    public boolean isAir() {
        return AIR.equals(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuiltinBlockState that = (BuiltinBlockState) o;
        return blockState.equals(that.blockState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockState);
    }
}
