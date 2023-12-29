package work.lclpnet.kibu.schematic;

import net.minecraft.block.BlockState;
import work.lclpnet.kibu.mc.KibuBlockState;
import work.lclpnet.kibu.util.BlockStateUtils;

import javax.annotation.Nonnull;

public class FabricKibuBlockState implements KibuBlockState {

    private transient final BlockState state;
    private volatile String string = null;

    public FabricKibuBlockState(BlockState state) {
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

    public BlockState getState() {
        return state;
    }
}
