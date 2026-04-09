package work.lclpnet.kibu.hook.mixin.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.level.BlockGetter;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.level.BlockModificationHooks;

@Mixin(RemoveBlockGoal.class)
public abstract class RemoveBlockGoalMixin {

    @Shadow private int ticksSinceReachedGoal;

    @Shadow @Final private Mob removerMob;

    @Shadow
    protected abstract @Nullable BlockPos getPosWithBlock(BlockPos pos, BlockGetter world);

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"
            ),
            cancellable = true
    )
    public void kibu$onDestroyBlock(CallbackInfo ci) {
        final var world = this.removerMob.level();
        final var pos = getPosWithBlock(this.removerMob.blockPosition(), world);

        if (BlockModificationHooks.TRAMPLE_TURTLE_EGG.invoker().onModify(world, pos, this.removerMob)) {
            ci.cancel();
            ++this.ticksSinceReachedGoal;
        }
    }

    @Inject(
            method = "canUse",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$interceptCanStart(CallbackInfoReturnable<Boolean> cir) {
        if (BlockModificationHooks.CAN_MOB_GRIEF.invoker().onModify(this.removerMob.level(), this.removerMob.blockPosition(), this.removerMob)) {
            cir.setReturnValue(false);
        }
    }
}
