package work.lclpnet.kibu.hook.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.WardenEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.EntityTargetCallback;

@Mixin(WardenEntity.class)
public class WardenEntityMixin {

    @Inject(
            method = "isValidTarget",
            at = @At("RETURN"),
            cancellable = true
    )
    public void kibu$onValidTarget(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ() || !(entity instanceof LivingEntity living)) return;

        WardenEntity self = (WardenEntity) (Object) this;

        if (EntityTargetCallback.HOOK.invoker().onChangeTarget(self, living)) {
            cir.setReturnValue(false);
        }
    }
}
