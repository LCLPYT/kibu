package work.lclpnet.kibu.schematic;

import work.lclpnet.kibu.mc.BlockState;
import work.lclpnet.kibu.util.BlockStateUtils;

import javax.annotation.Nonnull;

public class FabricBlockState implements BlockState {

    private transient final net.minecraft.block.BlockState state;
    private volatile String string = null;

    public FabricBlockState(net.minecraft.block.BlockState state) {
        this.state = state;
    }

    @Override
    public String getAsString() {
        if (string == null) {
            synchronized (this) {
                if (string == null) {
                    string = buildString();
                }
            }
        }

        return string;
    }

    @Nonnull
    private String buildString() {
        return BlockStateUtils.stringify(state);
    }

    @Override
    public boolean isAir() {
        return state.isAir();
    }

    public net.minecraft.block.BlockState getState() {
        return state;
    }
}
