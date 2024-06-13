package work.lclpnet.kibu.hook.mixin.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.AffectedByDaylightCallback;
import work.lclpnet.kibu.hook.entity.EntityTargetCallback;

@Mixin(MobEntity.class)
public class MobEntityMixin {

    @Inject(
            method = "isAffectedByDaylight",
            at = @At("RETURN"),
            cancellable = true
    )
    public void kibu$isAffectedByDaylight(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) return;

        MobEntity self = (MobEntity) (Object) this;

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
        MobEntity self = (MobEntity) (Object) this;

        if (EntityTargetCallback.HOOK.invoker().onChangeTarget(self, target)) {
            ci.cancel();
        }
    }
}
