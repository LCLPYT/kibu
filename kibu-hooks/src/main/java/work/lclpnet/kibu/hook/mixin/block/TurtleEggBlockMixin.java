package work.lclpnet.kibu.hook.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.level.BlockModificationHooks;

@Mixin(TurtleEggBlock.class)
public class TurtleEggBlockMixin {

    @Inject(
            method = "destroyEgg",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/TurtleEggBlock;decreaseEggs(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"
            ),
            cancellable = true
    )
    public void kibu$onBreakEgg(Level level, BlockState state, BlockPos pos, Entity entity, int randomness, CallbackInfo ci) {
        if (BlockModificationHooks.TRAMPLE_TURTLE_EGG.invoker().onModify(level, pos, entity)) {
            ci.cancel();
        }
    }
}
