package work.lclpnet.kibu.hook.mixin;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;

@Mixin(AbstractSignBlock.class)
public class AbstractSignBlockMixin {

    @Inject(
            method = "onUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/SignChangingItem;canUseOnSignText(Lnet/minecraft/block/entity/SignText;Lnet/minecraft/entity/player/PlayerEntity;)Z"
            ),
            cancellable = true
    )
    public void kibu$interceptOnUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        var ctx = new ItemUsageContext(player, hand, hit);
        var result = BlockModificationHooks.USE_ITEM_ON_BLOCK.invoker().onUse(ctx);

        if (result != null) {
            cir.setReturnValue(result);
        }
    }

    @Inject(
            method = "onUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractSignBlock;openEditScreen(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/block/entity/SignBlockEntity;Z)V"
            ),
            cancellable = true
    )
    public void kibu$onEditSign(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (BlockModificationHooks.EDIT_SIGN.invoker().onModify(world, pos, player)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
