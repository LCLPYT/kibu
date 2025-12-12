package work.lclpnet.kibu.hook.mixin.entity;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.player.PlayerInventoryHooks;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Inject(
            method = "playerTouch",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z"
            ),
            cancellable = true
    )
    public void kibu$onItemPickup(Player player, CallbackInfo ci) {
        @SuppressWarnings("DataFlowIssue")
        ItemEntity self = (ItemEntity) (Object) this;

        boolean cancel = PlayerInventoryHooks.PLAYER_PICKUP.invoker().onPickup(player, self);
        if (cancel) {
            ci.cancel();
        }
    }

    @Inject(
            method = "playerTouch",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;take(Lnet/minecraft/world/entity/Entity;I)V"
            )
    )
    public void kibu$onItemPickedUp(Player player, CallbackInfo ci) {
        @SuppressWarnings("DataFlowIssue")
        ItemEntity self = (ItemEntity) (Object) this;

        PlayerInventoryHooks.PLAYER_PICKED_UP.invoker().onPickedUp(player, self);
    }
}
