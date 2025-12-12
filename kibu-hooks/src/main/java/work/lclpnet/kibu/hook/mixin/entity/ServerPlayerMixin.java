package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.player.PlayerInventoryHooks;
import work.lclpnet.kibu.hook.player.PlayerMountHooks;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(
            method = "startRiding",
            at = @At("RETURN")
    )
    public void kibu$onStartedRiding(Entity vehicle, boolean force, boolean emitEvent, CallbackInfoReturnable<Boolean> cir) {
        if (vehicle == null || !cir.getReturnValue()) return;

        @SuppressWarnings("DataFlowIssue")
        ServerPlayer self = (ServerPlayer) (Object) this;

        PlayerMountHooks.MOUNTED.invoker().doAfter(self, vehicle);
    }

    @Inject(
            method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
            at = @At(value = "RETURN")
    )
    public void kibu$onDroppedItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir,
                                   @Local ItemEntity item) {
        if (item == null) return;

        @SuppressWarnings("DataFlowIssue")
        ServerPlayer self = (ServerPlayer) (Object) this;

        PlayerInventoryHooks.DROPPED_ITEM_ENTITY.invoker().onDroppedItemEntity(self, item);
    }
}
