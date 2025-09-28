package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Portal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityAttachS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.EntityDismountCallback;
import work.lclpnet.kibu.hook.entity.EntityMountCallback;
import work.lclpnet.kibu.hook.entity.EntityRemovedCallback;
import work.lclpnet.kibu.hook.entity.EntityUsePortalCallback;
import work.lclpnet.kibu.hook.entity.leash.LeashDestroyCallback;
import work.lclpnet.kibu.hook.entity.leash.LeashEntitiesToEntityCallback;
import work.lclpnet.kibu.hook.entity.leash.LeashEntityCallback;
import work.lclpnet.kibu.hook.entity.leash.UnleashEntityCallback;
import work.lclpnet.kibu.hook.player.PlayerSneakCallback;
import work.lclpnet.kibu.hook.player.PlayerSprintCallback;
import work.lclpnet.kibu.hook.util.MixinUtils;
import work.lclpnet.kibu.hook.util.PlayerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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
            method = "dropStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/entity/ItemEntity;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    public boolean kibu$onDropItem(ServerWorld instance, Entity entity, Operation<Boolean> original) {
        return MixinUtils.wrapEntityItemDrop(instance, entity, original, this);
    }

    @Inject(
            method = "startRiding(Lnet/minecraft/entity/Entity;ZZ)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;hasVehicle()Z"
            ),
            cancellable = true
    )
    public void kibu$onStartRiding(Entity entity, boolean force, boolean emitEvent, CallbackInfoReturnable<Boolean> cir) {
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
                    target = "Lnet/minecraft/entity/Leashable;isLeashed()Z"
            ),
            cancellable = true
    )
    public void kibu$onLeash(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        Entity self = (Entity) (Object) this;

        if (LeashEntityCallback.HOOK.invoker().onLeash(player, self)) {
            cir.setReturnValue(ActionResult.PASS);

            // fix de-sync
            if (player instanceof ServerPlayerEntity serverPlayer) {
                PlayerUtils.syncPlayerItems(player);
                Entity leashHolder = ((Leashable) self).getLeashHolder();
                serverPlayer.networkHandler.sendPacket(new EntityAttachS2CPacket(self, leashHolder));
            }
        }
    }

    @Inject(
            method = "interact",
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Leashable;detachLeash()V",
                    ordinal = 0
            ), @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Leashable;detachLeashWithoutDrop()V"
            )},
            cancellable = true
    )
    public void kibu$beforeUnleashMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        Entity self = (Entity) (Object) this;

        if (UnleashEntityCallback.HOOK.invoker().onUnleash(player, self)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }

    @Inject(
            method = "detachAllHeldLeashes",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onDestroyLeash(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        var self = (Entity) (Object) this;

        if (LeashDestroyCallback.HOOK.invoker().onLeashDestroy(player, self)) {
            cir.setReturnValue(false);
        }
    }

    @WrapOperation(
            method = "interact",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Leashable;collectLeashablesAround(Lnet/minecraft/entity/Entity;Ljava/util/function/Predicate;)Ljava/util/List;"
            )
    )
    public List<Leashable> kibu$collectEntitiesToLeash(Entity leashHolder, Predicate<Leashable> leashablePredicate, Operation<List<Leashable>> original,
                                                       @Local(argsOnly = true) PlayerEntity player) {

        List<Leashable> list = original.call(leashHolder, leashablePredicate);
        List<Entity> entities = new ArrayList<>(list.size());

        for (Leashable leashable : list) {
            if (leashable instanceof Entity entity) {
                entities.add(entity);
            }
        }

        if (LeashEntitiesToEntityCallback.HOOK.invoker().onLeashToEntity(player, leashHolder, entities)) {
            // cancelled, return empty list so that caller continues
            return List.of();
        }

        return list;
    }

    @Inject(
            method = "tryUsePortal",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$beforeUsePortal(Portal portal, BlockPos pos, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;

        if (self.hasPortalCooldown()) return;

        if (EntityUsePortalCallback.HOOK.invoker().onUsePortal(self, portal, pos)) {
            ci.cancel();
        }
    }
}
