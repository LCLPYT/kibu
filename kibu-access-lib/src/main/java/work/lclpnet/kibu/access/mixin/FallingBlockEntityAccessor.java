package work.lclpnet.kibu.access.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FallingBlockEntity.class)
public interface FallingBlockEntityAccessor {

    @Accessor
    void setBlock(BlockState state);

    @Accessor
    void setDestroyedOnLanding(boolean destroyedOnLanding);

    @Accessor
    boolean getDestroyedOnLanding();
}
