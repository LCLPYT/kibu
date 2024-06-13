package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityAttachS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.*;
import work.lclpnet.kibu.hook.player.PlayerSneakCallback;
import work.lclpnet.kibu.hook.player.PlayerSprintCallback;
import work.lclpnet.kibu.hook.util.MixinUtils;
import work.lclpnet.kibu.hook.util.PlayerUtils;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(
            method = "setRemoved",
            at = @At("TAIL")
    )
    public void kibu$onRemoved(Entity.RemovalReason reason, CallbackInfo ci) {
        @SuppressWarnings("DataFlowIssue")
        Entity self = (Entity) (Object) this;

        EntityRemovedCallback.HOOK.invoker().onRemove(self, reason);
    }

    @Inject(
            method = "setSneaking",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onSneak(boolean sneaking, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;

        if (!(self instanceof ServerPlayerEntity serverPlayer)) return;

        if (PlayerSneakCallback.HOOK.invoker().onSneak(serverPlayer, sneaking)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "setSprinting",
            at = @At("HEAD")
    )
    public void kibu$onSprint(boolean sneaking, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;

        if (!(self instanceof ServerPlayerEntity serverPlayer)) return;

        PlayerSprintCallback.HOOK.invoker().onSprint(serverPlayer, sneaking);
    }

    @WrapOperation(
            method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    public boolean kibu$onDropItem(World world, Entity entity, Operation<Boolean> original) {
        return MixinUtils.wrapEntityItemDrop(world, entity, original, this);
    }

    @Inject(
            method = "startRiding(Lnet/minecraft/entity/Entity;Z)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;hasVehicle()Z"
            ),
            cancellable = true
    )
    public void kibu$onStartRiding(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir) {
        Entity self = (Entity) (Object) this;

        if (EntityMountCallback.HOOK.invoker().onMount(self, entity, force)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "stopRiding",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onStopRiding(CallbackInfo ci) {
        Entity self = (Entity) (Object) this;

        // LivingEntity is handled in LivingEntityMixin
        if (self instanceof LivingEntity) return;

        Entity vehicle = self.getVehicle();

        if (EntityDismountCallback.HOOK.invoker().onDismount(self, vehicle)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "interact",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Leashable;attachLeash(Lnet/minecraft/entity/Entity;Z)V"
            ),
            cancellable = true
    )
    public void kibu$beforeLeashMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        Entity self = (Entity) (Object) this;

        if (LeashEntityCallback.HOOK.invoker().onLeash(player, (Leashable) self)) {
            cir.setReturnValue(ActionResult.PASS);

            // fix de-sync
            if (player instanceof ServerPlayerEntity serverPlayer) {
                PlayerUtils.syncPlayerItems(player);
                serverPlayer.networkHandler.sendPacket(new EntityAttachS2CPacket(self, null));
            }
        }
    }

    @Inject(
            method = "interact",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Leashable;detachLeash(ZZ)V"
            ),
            cancellable = true
    )
    public void kibu$beforeUnleashMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        Leashable self = (Leashable) this;

        if (UnleashEntityCallback.HOOK.invoker().onUnleash(player, self)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
