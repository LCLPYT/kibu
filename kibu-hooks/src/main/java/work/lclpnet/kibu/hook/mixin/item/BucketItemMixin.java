package work.lclpnet.kibu.hook.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.util.PlayerUtils;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;

@Mixin(BucketItem.class)
public class BucketItemMixin {

    @Shadow @Final private Fluid content;

    @Inject(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BucketPickup;pickupBlock(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/item/ItemStack;"
            ),
            cancellable = true
    )
    public void kibu$onPickupFluid(Level world, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir,
                                   @Local(ordinal = 0) BlockPos pos) {

        if (BlockModificationHooks.PICKUP_FLUID.invoker().onTransfer(world, pos, player, content)) {
            cir.setReturnValue(InteractionResult.FAIL);

            if (player instanceof ServerPlayer) {
                ((ServerPlayer) player).connection.send(new ClientboundBlockUpdatePacket(world, pos));
                PlayerUtils.syncPlayerItems(player);
            }
        }
    }

    @Inject(
            method = "emptyContents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/dimension/DimensionType;ultraWarm()Z"
            ),
            cancellable = true
    )
    public void kibu$onPlaceFluid(LivingEntity user, Level world, BlockPos pos, BlockHitResult hitResult, CallbackInfoReturnable<Boolean> cir) {
        if (BlockModificationHooks.PLACE_FLUID.invoker().onTransfer(world, pos, user, content)) {
            cir.setReturnValue(false);

            if (user instanceof ServerPlayer player) {
                player.connection.send(new ClientboundBlockUpdatePacket(player.level(), pos));
                PlayerUtils.syncPlayerItems(player);
            }
        }
    }
}
