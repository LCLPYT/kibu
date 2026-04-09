package work.lclpnet.kibu.hook.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;

@Mixin(SignBlock.class)
public class AbstractSignBlockMixin {

    @Inject(
            method = "useItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/SignApplicator;canApplyToSign(Lnet/minecraft/world/level/block/entity/SignText;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;)Z"
            ),
            cancellable = true
    )
    public void kibu$interceptOnUse(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        var ctx = new UseOnContext(player, hand, hitResult);
        var result = BlockModificationHooks.USE_ITEM_ON_BLOCK.invoker().onUse(ctx);

        if (result != null) {
            cir.setReturnValue(result);
        }
    }

    @Inject(
            method = "useWithoutItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/SignBlock;openTextEdit(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/entity/SignBlockEntity;Z)V"
            ),
            cancellable = true
    )
    public void kibu$onEditSign(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (BlockModificationHooks.EDIT_SIGN.invoker().onModify(level, pos, player)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
