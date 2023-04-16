package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.ItemEntityHooks;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Inject(
            method = "onPlayerCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"
            ),
            cancellable = true
    )
    public void kibu$onItemPickup(PlayerEntity player, CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;
        boolean cancel = ItemEntityHooks.PLAYER_PICKUP.invoker().onPickup(player, self);
        if (cancel) {
            ci.cancel();
        }
    }

    @Inject(
            method = "onPlayerCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;sendPickup(Lnet/minecraft/entity/Entity;I)V"
            )
    )
    public void kibu$onItemPickedUp(PlayerEntity player, CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;
        ItemEntityHooks.PLAYER_PICKED_UP.invoker().onPickedUp(player, self);
    }
}
