package work.lclpnet.kibu.access.mixin;

import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FallingBlockEntity.class)
public interface FallingBlockEntityAccessor {

    @Accessor
    void setBlockState(BlockState state);

    @Accessor
    void setCancelDrop(boolean destroyedOnLanding);

    @Accessor
    boolean getCancelDrop();
}
