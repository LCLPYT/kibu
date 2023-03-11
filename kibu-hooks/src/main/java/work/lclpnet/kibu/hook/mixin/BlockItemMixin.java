package work.lclpnet.kibu.hook.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import work.lclpnet.kibu.hook.util.PlayerUtils;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(
            method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/BlockItem;place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z"
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void onPlaceBlock(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir, ItemPlacementContext modifiedContext, BlockState newState) {
        final PlayerEntity player = context.getPlayer();

        if (BlockModificationHooks.PLACE_BLOCK.invoker().onPlace(context.getWorld(), context.getBlockPos(), player, newState)) {
            cir.setReturnValue(ActionResult.FAIL);

            if (player != null) {
                PlayerUtils.syncPlayerItems(player);
            }
        }
    }
}
