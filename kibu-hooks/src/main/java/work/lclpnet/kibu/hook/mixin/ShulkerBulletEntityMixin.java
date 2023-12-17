package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.NonLivingDamageCallback;

@Mixin(ShulkerBulletEntity.class)
public class ShulkerBulletEntityMixin {

    @Inject(
            method = "damage",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$beforeDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ShulkerBulletEntity self = (ShulkerBulletEntity) (Object) this;

        if (NonLivingDamageCallback.HOOK.invoker().onDamage(self, source, amount)) {
            cir.setReturnValue(false);
        }
    }
}
