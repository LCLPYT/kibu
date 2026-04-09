package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.EntityDamageCallback;
import work.lclpnet.kibu.hook.entity.EntityDismountCallback;
import work.lclpnet.kibu.hook.entity.EntityHealthCallback;
import work.lclpnet.kibu.hook.entity.EntityStatusEffectCallback;
import work.lclpnet.kibu.hook.player.PlayerInventoryHooks;
import work.lclpnet.kibu.hook.player.PlayerMountHooks;
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
            method = "createWitherRose",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
            )
    )
    public boolean kibu$onDropItem(Level world, Entity entity, Operation<Boolean> original) {
        return MixinUtils.wrapEntityItemDrop(world, entity, original, this);
    }

    @Inject(
            method = "actuallyHurt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"
            ),
            cancellable = true
    )
    public void kibu$onDamage(ServerLevel level, DamageSource source, float dmg, CallbackInfo ci) {
        @SuppressWarnings("DataFlowIssue")
        LivingEntity entity = (LivingEntity) (Object) this;

        if (EntityDamageCallback.HOOK.invoker().onDamage(entity, source, dmg)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
            ),
            cancellable = true
    )
    public void kibu$onAddStatusEffect(MobEffectInstance newEffect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (EntityStatusEffectCallback.HOOK.invoker().onAddEffect(self, newEffect, source)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "stopRiding",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onStopRiding(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        Entity vehicle = self.getVehicle();

        if (EntityDismountCallback.HOOK.invoker().onDismount(self, vehicle)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "stopRiding",
            at = @At("TAIL")
    )
    public void kibu$onStoppedRiding(CallbackInfo ci, @Local(name = "oldVehicle") Entity oldVehicle) {
        if (oldVehicle == null) return;

        LivingEntity self = (LivingEntity) (Object) this;

        if (self instanceof ServerPlayer player) {
            PlayerMountHooks.DISMOUNTED.invoker().doAfter(player, oldVehicle);
        }
    }

    @Inject(
            method = "drop",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
            ),
            cancellable = true
    )
    public void kibu$onDropItem(ItemStack itemStack, boolean randomly, boolean thrownFromHand, CallbackInfoReturnable<ItemEntity> cir, @Local(name = "entity") ItemEntity entity) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (self instanceof ServerPlayer player && PlayerInventoryHooks.DROP_ITEM_ENTITY.invoker().onDropItemEntity(player, entity)) {
            cir.setReturnValue(null);
        }
    }
}
