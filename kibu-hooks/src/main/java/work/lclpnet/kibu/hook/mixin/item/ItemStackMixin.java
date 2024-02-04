package work.lclpnet.kibu.hook.mixin.item;

import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import work.lclpnet.kibu.hook.entity.ItemUseOnEntityCallback;
import work.lclpnet.kibu.hook.util.PlayerUtils;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(
            method = "useOnBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/Item;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;"
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void kibu$interceptUseOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir, PlayerEntity playerEntity, BlockPos blockPos, CachedBlockPosition cachedBlockPosition, Item item) {
        ActionResult result = BlockModificationHooks.USE_ITEM_ON_BLOCK.invoker().onUse(context);

        if (result != null) {
            // when useOnBlock is cancelled, sync the item consumption cancel with the client
            if (!playerEntity.isCreative() && !playerEntity.isSpectator()) {
                PlayerUtils.syncPlayerItems(playerEntity);
            }

            cir.setReturnValue(result);
        }
    }

    @Inject(
            method = "useOnEntity",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$interceptUseOnEntity(PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        if (ItemUseOnEntityCallback.HOOK.invoker().onUseOnEntity(user, entity, hand, stack)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
