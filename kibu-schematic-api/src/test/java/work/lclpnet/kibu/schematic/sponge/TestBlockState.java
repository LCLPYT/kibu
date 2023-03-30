package work.lclpnet.kibu.schematic.sponge;

import work.lclpnet.kibu.mc.BlockState;

public class TestBlockState implements BlockState {

    private final String string;

    public TestBlockState(String string) {
        this.string = string;
    }

    @Override
    public String getAsString() {
        return string;
    }

    @Override
    public boolean isAir() {
        return false;  // always serialize
    }
}
