package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.EntityDamageCallback;
import work.lclpnet.kibu.hook.entity.EntityHealthCallback;
import work.lclpnet.kibu.hook.util.MixinUtils;

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

    @WrapOperation(
            method = "onKilledBy",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    public boolean kibu$onDropItem(World world, Entity entity, Operation<Boolean> original) {
        return MixinUtils.wrapEntityItemDrop(world, entity, original, this);
    }

    @Inject(
            method = "applyDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;applyArmorToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"
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
