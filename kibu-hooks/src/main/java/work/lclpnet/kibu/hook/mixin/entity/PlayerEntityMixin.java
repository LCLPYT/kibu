package work.lclpnet.kibu.hook.mixin.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.EntityDamageCallback;
import work.lclpnet.kibu.hook.type.PlayerAware;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Shadow protected HungerManager hungerManager;

    @Inject(
            method = "<init>*",
            at = @At("RETURN")
    )
    public void kibu$onInit(CallbackInfo ci) {
        //noinspection DataFlowIssue
        ((PlayerAware) hungerManager).kibu$setPlayer((PlayerEntity) (Object) this);
    }

    @Inject(
            method = "applyDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;applyArmorToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"
            ),
            cancellable = true
    )
    public void kibu$onDamage(DamageSource source, float amount, CallbackInfo ci) {
        @SuppressWarnings("DataFlowIssue")
        LivingEntity entity = (LivingEntity) (Object) this;

        if (EntityDamageCallback.HOOK.invoker().onDamage(entity, source, amount)) {
            ci.cancel();
        }
    }
}
