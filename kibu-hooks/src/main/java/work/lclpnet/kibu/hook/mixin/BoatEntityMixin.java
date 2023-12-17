package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.BoatDamageCallback;

@Mixin(BoatEntity.class)
public class BoatEntityMixin {

    @Inject(
            method = "damage",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$beforeDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        BoatEntity self = (BoatEntity) (Object) this;

        if (BoatDamageCallback.HOOK.invoker().onDamage(self, source, amount)) {
            cir.setReturnValue(false);
        }
    }
}
