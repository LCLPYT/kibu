package work.lclpnet.kibu.hook.mixin.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.NonLivingDamageCallback;

@Mixin(ShulkerBullet.class)
public class ShulkerBulletMixin {

    @Inject(
            method = "hurtServer",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$beforeDamage(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        ShulkerBullet self = (ShulkerBullet) (Object) this;

        if (NonLivingDamageCallback.HOOK.invoker().onDamage(self, source, damage)) {
            cir.setReturnValue(false);
        }
    }
}
