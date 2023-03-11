package work.lclpnet.kibu.hook.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.IceBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
    public void onMelt(BlockState state, World world, BlockPos pos, CallbackInfo ci) {
        if (WorldPhysicsHooks.MELT.invoker().onFade(world, pos))
            ci.cancel();
    }
}
