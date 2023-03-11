package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.model.BlockModification;
import work.lclpnet.kibu.hook.model.BlockModificationType;
import work.lclpnet.kibu.hook.util.PlayerUtils;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(
            method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onPlaceBlock(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        final PlayerEntity player = context.getPlayer();
        BlockModification data = new BlockModification(BlockModificationType.PLACE_BLOCK, context.getWorld(), context.getBlockPos(), player);

        if (BlockModificationHooks.MODIFY_BLOCK.invoker().onModify(data)) {
            cir.setReturnValue(ActionResult.FAIL);

            if (player != null) {
                PlayerUtils.syncPlayerItems(player);
            }
        }
    }
}
