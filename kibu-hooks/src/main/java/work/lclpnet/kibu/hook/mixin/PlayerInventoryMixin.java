package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.player.PlayerInventoryHooks;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @Shadow @Final public PlayerEntity player;

    @Shadow public int selectedSlot;

    @Inject(
            method = "dropSelectedItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerInventory;removeStack(II)Lnet/minecraft/item/ItemStack;"
            ),
            cancellable = true
    )
    public void kibu$onDropSelectedItem(boolean entireStack, CallbackInfoReturnable<ItemStack> cir) {
        boolean cancel = PlayerInventoryHooks.DROP_ITEM.invoker().onDropItem(player, selectedSlot);

        if (cancel) {
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }

    @Inject(
            method = "dropSelectedItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerInventory;removeStack(II)Lnet/minecraft/item/ItemStack;",
                    shift = At.Shift.AFTER
            )
    )
    public void kibu$onDroppedSelectedItem(boolean entireStack, CallbackInfoReturnable<ItemStack> cir) {
        PlayerInventoryHooks.DROPPED_ITEM.invoker().onDroppedItem(player, selectedSlot);
    }
}
