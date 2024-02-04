package work.lclpnet.kibu.hook.mixin.entity;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.EntityHealthCallback;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(
            method = "setHealth",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onSetHealth(float health, CallbackInfo ci) {
        @SuppressWarnings("DataFlowIssue")
        LivingEntity entity = (LivingEntity) (Object) this;

        if (EntityHealthCallback.HOOK.invoker().onHealthChange(entity, health)) {
            ci.cancel();
        }
    }
}
