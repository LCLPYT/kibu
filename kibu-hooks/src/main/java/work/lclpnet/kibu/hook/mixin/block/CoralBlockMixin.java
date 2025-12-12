package work.lclpnet.kibu.hook.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CoralBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

@Mixin(CoralBlock.class)
public class CoralBlockMixin {

    @Inject(
            method = "scanForWater",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ruler$isInWater(BlockGetter blockView, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!(blockView instanceof Level world)) return;

        boolean cancelled = WorldPhysicsHooks.CORAL_DEATH.invoker().onFade(world, pos);

        if (cancelled) {
            cir.setReturnValue(true);
        }
    }
}
