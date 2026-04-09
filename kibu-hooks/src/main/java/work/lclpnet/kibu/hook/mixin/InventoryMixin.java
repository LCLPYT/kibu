package work.lclpnet.kibu.hook.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.player.PlayerInventoryHooks;
import work.lclpnet.kibu.hook.util.PlayerUtils;

@Mixin(Inventory.class)
public class InventoryMixin {

    @Shadow @Final public Player player;

    @Shadow private int selected;

    @Inject(
            method = "removeFromSelected",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Inventory;removeItem(II)Lnet/minecraft/world/item/ItemStack;"
            ),
            cancellable = true
    )
    public void kibu$onDropSelectedItem(boolean all, CallbackInfoReturnable<ItemStack> cir) {
        boolean cancel = PlayerInventoryHooks.DROP_ITEM.invoker().onDropItem(player, selected, false);

        if (cancel) {
            cir.setReturnValue(ItemStack.EMPTY);
            PlayerUtils.syncPlayerItems(player);
        }
    }

    @Inject(
            method = "removeFromSelected",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Inventory;removeItem(II)Lnet/minecraft/world/item/ItemStack;",
                    shift = At.Shift.AFTER
            )
    )
    public void kibu$onDroppedSelectedItem(boolean all, CallbackInfoReturnable<ItemStack> cir) {
        PlayerInventoryHooks.DROPPED_ITEM.invoker().onDroppedItem(player, selected);
    }
}
