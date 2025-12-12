package work.lclpnet.kibu.hook.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

@Mixin(IceBlock.class)
public class IceBlockMixin {

    @Inject(
            method = "melt",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onMelt(BlockState state, Level world, BlockPos pos, CallbackInfo ci) {
        if (WorldPhysicsHooks.MELT.invoker().onFade(world, pos))
            ci.cancel();
    }
}
