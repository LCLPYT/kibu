package work.lclpnet.kibu.hook.mixin.ai;

import net.minecraft.entity.ai.goal.StepAndDestroyBlockGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;

@Mixin(StepAndDestroyBlockGoal.class)
public abstract class StepAndDestroyBlockGoalMixin {

    @Shadow private int counter;

    @Shadow @Final private MobEntity stepAndDestroyMob;

    @Shadow
    protected abstract @Nullable BlockPos tweakToProperPos(BlockPos pos, BlockView world);

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"
            ),
            cancellable = true
    )
    public void kibu$onDestroyBlock(CallbackInfo ci) {
        final var world = this.stepAndDestroyMob.getWorld();
        final var pos = tweakToProperPos(this.stepAndDestroyMob.getBlockPos(), world);

        if (BlockModificationHooks.TRAMPLE_TURTLE_EGG.invoker().onModify(world, pos, this.stepAndDestroyMob)) {
            ci.cancel();
            ++this.counter;
        }
    }

    @Inject(
            method = "canStart",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$interceptCanStart(CallbackInfoReturnable<Boolean> cir) {
        if (BlockModificationHooks.CAN_MOB_GRIEF.invoker().onModify(this.stepAndDestroyMob.getWorld(), this.stepAndDestroyMob.getBlockPos(), this.stepAndDestroyMob)) {
            cir.setReturnValue(false);
        }
    }
}
