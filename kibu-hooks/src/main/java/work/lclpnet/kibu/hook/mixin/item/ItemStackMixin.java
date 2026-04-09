package work.lclpnet.kibu.hook.mixin.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.ItemUseOnEntityCallback;
import work.lclpnet.kibu.hook.util.PlayerUtils;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(
            method = "useOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Item;useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;"
            ),
            cancellable = true
    )
    public void kibu$interceptUseOnBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        InteractionResult result = BlockModificationHooks.USE_ITEM_ON_BLOCK.invoker().onUse(context);

        if (result == null) return;

        Player player = context.getPlayer();

        if (player == null) return;

        // when useOnBlock is cancelled, sync the item consumption cancel with the client
        if (!player.isCreative() && !player.isSpectator()) {
            PlayerUtils.syncPlayerItems(player);
        }

        cir.setReturnValue(result);
    }

    @Inject(
            method = "interactLivingEntity",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$interceptUseOnEntity(Player player, LivingEntity target, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        if (ItemUseOnEntityCallback.HOOK.invoker().onUseOnEntity(player, target, hand, stack)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
