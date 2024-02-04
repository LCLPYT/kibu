package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import work.lclpnet.kibu.hook.player.PlayerInventoryHooks;
import work.lclpnet.kibu.hook.player.PlayerMountHooks;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(
            method = "startRiding",
            at = @At("RETURN")
    )
    public void kibu$onStartedRiding(Entity vehicle, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (vehicle == null || !cir.getReturnValue()) return;

        @SuppressWarnings("DataFlowIssue")
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;

        PlayerMountHooks.MOUNTED.invoker().doAfter(self, vehicle);
    }

    @Inject(
            method = "stopRiding",
            at = @At("TAIL"),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    public void kibu$onStoppedRiding(CallbackInfo ci, Entity vehicle) {
        if (vehicle == null) return;

        @SuppressWarnings("DataFlowIssue")
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;

        PlayerMountHooks.DISMOUNTED.invoker().doAfter(self, vehicle);
    }

    @Inject(
            method = "dropItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void kibu$onDropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir, ItemEntity item) {
        @SuppressWarnings("DataFlowIssue")
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;

        if (PlayerInventoryHooks.DROP_ITEM_ENTITY.invoker().onDropItemEntity(self, item)) {
            cir.setReturnValue(null);
        }
    }

    @Inject(
            method = "dropItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z",
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void kibu$onDroppedItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir, ItemEntity item) {
        @SuppressWarnings("DataFlowIssue")
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;

        PlayerInventoryHooks.DROPPED_ITEM_ENTITY.invoker().onDroppedItemEntity(self, item);
    }
}
