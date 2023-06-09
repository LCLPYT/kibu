package work.lclpnet.kibu.hook.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

@Mixin(SnowBlock.class)
public class SnowBlockMixin {

    @Inject(
            method = "randomTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/SnowBlock;dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"
            ),
            cancellable = true
    )
    public void kibu$onMelt(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (WorldPhysicsHooks.MELT.invoker().onFade(world, pos))
            ci.cancel();
    }
}
