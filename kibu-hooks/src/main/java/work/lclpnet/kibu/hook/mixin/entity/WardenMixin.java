package work.lclpnet.kibu.hook.mixin.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.EntityTargetCallback;

@Mixin(Warden.class)
public class WardenMixin {

    @Inject(
            method = "canTargetEntity",
            at = @At("RETURN"),
            cancellable = true
    )
    public void kibu$onValidTarget(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ() || !(entity instanceof LivingEntity living)) return;

        Warden self = (Warden) (Object) this;

        if (EntityTargetCallback.HOOK.invoker().onChangeTarget(self, living)) {
            cir.setReturnValue(false);
        }
    }
}
