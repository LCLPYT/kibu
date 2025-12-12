package work.lclpnet.kibu.hook.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseCoralPlantTypeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

@Mixin(BaseCoralPlantTypeBlock.class)
public class BaseCoralPlantTypeBlockMixin {

    @Inject(
            method = "scanForWater",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void ruler$isInWater(BlockState state, BlockGetter blockView, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!(blockView instanceof Level world)) return;

        boolean cancelled = WorldPhysicsHooks.CORAL_DEATH.invoker().onFade(world, pos);

        if (cancelled) {
            cir.setReturnValue(true);
        }
    }
}
