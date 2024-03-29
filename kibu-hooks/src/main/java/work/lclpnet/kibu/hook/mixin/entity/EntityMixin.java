package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.EntityRemovedCallback;
import work.lclpnet.kibu.hook.player.PlayerSneakCallback;
import work.lclpnet.kibu.hook.player.PlayerSprintCallback;
import work.lclpnet.kibu.hook.util.MixinUtils;

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
}
