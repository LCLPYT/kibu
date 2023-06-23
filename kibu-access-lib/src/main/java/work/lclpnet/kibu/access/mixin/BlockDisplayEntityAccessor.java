package work.lclpnet.kibu.access.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.decoration.DisplayEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DisplayEntity.BlockDisplayEntity.class)
public interface BlockDisplayEntityAccessor {

    @Invoker
    void invokeSetBlockState(BlockState state);

    @Invoker
    BlockState invokeGetBlockState();
}
