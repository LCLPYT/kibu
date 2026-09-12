package work.lclpnet.kibu.hook.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShelfBlock;
import net.minecraft.world.level.block.entity.ShelfBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.level.ShelfPlaceItemCallback;
import work.lclpnet.kibu.hook.level.ShelfTakeItemCallback;

import java.util.OptionalInt;

@Mixin(ShelfBlock.class)
public class ShelfBlockMixin {

    @Inject(
            method = "useItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/ShelfBlock;swapSingleItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/entity/ShelfBlockEntity;ILnet/minecraft/world/entity/player/Inventory;)Z"
            ),
            cancellable = true
    )
    public void kibu$onSwapSingleItem(
            ItemStack itemStack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult,
            CallbackInfoReturnable<InteractionResult> cir,
            @Local ShelfBlockEntity shelf,
            @Local OptionalInt hitSlot
    ) {
        if (hitSlot.isPresent() && kibu$isSwapCancelled(level, pos, hitSlot.getAsInt(), itemStack, player, shelf)) {
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }

    @WrapOperation(
            method = "swapHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/ShelfBlockEntity;swapItemNoUpdate(ILnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"
            )
    )
    private ItemStack kibu$onSwapHotbarItem(
            ShelfBlockEntity shelf,
            int slot,
            ItemStack heldItemStack,
            Operation<ItemStack> original,
            @Local(argsOnly = true, name = "level") Level level,
            @Local(argsOnly = true, name = "inventory") Inventory inventory
    ) {
        if (kibu$isSwapCancelled(level, shelf.getBlockPos(), slot, heldItemStack, inventory.player, shelf)) {
            // returning the stack that was taken out of the hotbar restores it, leaving the shelf untouched
            return heldItemStack;
        }

        return original.call(shelf, slot, heldItemStack);
    }

    @Unique
    private static boolean kibu$isSwapCancelled(
            Level level,
            BlockPos pos,
            int slot,
            ItemStack heldStack,
            Player player,
            ShelfBlockEntity shelf
    ) {
        ItemStack shelfStack = shelf.getItem(slot);

        if (!shelfStack.isEmpty() && ShelfTakeItemCallback.HOOK.invoker().onTakeItem(level, pos, slot, shelfStack, player)) {
            return true;
        }

        return !heldStack.isEmpty() && ShelfPlaceItemCallback.HOOK.invoker().onPlaceItem(level, pos, slot, heldStack, player);
    }
}
