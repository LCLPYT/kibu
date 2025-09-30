package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.player.PlayerInventoryHooks;
import work.lclpnet.kibu.hook.player.PlayerMountHooks;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(
            method = "startRiding",
            at = @At("RETURN")
    )
    public void kibu$onStartedRiding(Entity vehicle, boolean force, boolean emitEvent, CallbackInfoReturnable<Boolean> cir) {
        if (vehicle == null || !cir.getReturnValue()) return;

        @SuppressWarnings("DataFlowIssue")
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;

        PlayerMountHooks.MOUNTED.invoker().doAfter(self, vehicle);
    }

    @Inject(
            method = "dropItem",
            at = @At(value = "RETURN")
    )
    public void kibu$onDroppedItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir,
                                   @Local ItemEntity item) {
        if (item == null) return;

        @SuppressWarnings("DataFlowIssue")
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;

        PlayerInventoryHooks.DROPPED_ITEM_ENTITY.invoker().onDroppedItemEntity(self, item);
    }
}
