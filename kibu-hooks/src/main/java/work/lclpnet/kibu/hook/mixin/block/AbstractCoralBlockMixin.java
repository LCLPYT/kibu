package work.lclpnet.kibu.hook.mixin.block;

import net.minecraft.block.AbstractCoralBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

@Mixin(AbstractCoralBlock.class)
public class AbstractCoralBlockMixin {

    @Inject(
            method = "isInWater",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void ruler$isInWater(BlockState state, BlockView blockView, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!(blockView instanceof World world)) return;

        boolean cancelled = WorldPhysicsHooks.CORAL_DEATH.invoker().onFade(world, pos);

        if (cancelled) {
            cir.setReturnValue(true);
        }
    }
}
