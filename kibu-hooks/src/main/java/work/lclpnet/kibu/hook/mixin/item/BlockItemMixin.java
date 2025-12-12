package work.lclpnet.kibu.hook.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.util.PlayerUtils;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(
            method = "place(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/InteractionResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/BlockItem;placeBlock(Lnet/minecraft/world/item/context/BlockPlaceContext;Lnet/minecraft/world/level/block/state/BlockState;)Z"
            ),
            cancellable = true
    )
    public void kibu$onPlaceBlock(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir, @Local BlockState newState) {
        final Player player = context.getPlayer();

        if (BlockModificationHooks.PLACE_BLOCK.invoker().onPlace(context.getLevel(), context.getClickedPos(), player, newState)) {
            cir.setReturnValue(InteractionResult.FAIL);

            if (player != null) {
                PlayerUtils.syncPlayerItems(player);
            }
        }
    }

    @Inject(
            method = "placeBlock(Lnet/minecraft/world/item/context/BlockPlaceContext;Lnet/minecraft/world/level/block/state/BlockState;)Z",
            at = @At("RETURN")
    )
    public void kibu$onBlockPlaced(BlockPlaceContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) return;

        BlockModificationHooks.BLOCK_PLACED.invoker().onModified(context.getLevel(), context.getClickedPos(), context.getPlayer());
    }
}
