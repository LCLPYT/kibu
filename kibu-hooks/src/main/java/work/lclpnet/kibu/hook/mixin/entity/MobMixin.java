package work.lclpnet.kibu.hook.mixin.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.AffectedByDaylightCallback;
import work.lclpnet.kibu.hook.entity.EntityTargetCallback;

@Mixin(Mob.class)
public class MobMixin {

    @Inject(
            method = "isSunBurnTick",
            at = @At("RETURN"),
            cancellable = true
    )
    public void kibu$isAffectedByDaylight(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) return;

        Mob self = (Mob) (Object) this;

        if (AffectedByDaylightCallback.HOOK.invoker().shouldIgnoreDaylight(self)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "setTarget",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onSetTarget(LivingEntity target, CallbackInfo ci) {
        Mob self = (Mob) (Object) this;

        if (EntityTargetCallback.HOOK.invoker().onChangeTarget(self, target)) {
            ci.cancel();
        }
    }
}
