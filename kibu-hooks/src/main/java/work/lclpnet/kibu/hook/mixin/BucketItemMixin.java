package work.lclpnet.kibu.hook.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import work.lclpnet.kibu.hook.model.BlockModification;
import work.lclpnet.kibu.hook.model.BlockModificationType;
import work.lclpnet.kibu.hook.util.PlayerUtils;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;

@Mixin(BucketItem.class)
public class BucketItemMixin {

    @Inject(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/FluidDrainable;tryDrainFluid(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/item/ItemStack;"
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void onPickupFluid(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir,
                              ItemStack itemStack, BlockHitResult blockHitResult, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState blockState, FluidDrainable fluidDrainable) {

        final BlockModification data = new BlockModification(BlockModificationType.PICKUP_FLUID, world, blockPos, user);

        if (BlockModificationHooks.MODIFY_BLOCK.invoker().onModify(data)) {
            cir.setReturnValue(TypedActionResult.fail(itemStack));

            if (user instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) user).networkHandler.sendPacket(new BlockUpdateS2CPacket(world, blockPos));
                PlayerUtils.syncPlayerItems(user);
            }
        }
    }

    @Inject(
            method = "placeFluid",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/dimension/DimensionType;ultrawarm()Z"
            ),
            cancellable = true
    )
    public void onPlaceFluid(PlayerEntity player, World world, BlockPos pos, BlockHitResult blockHitResult, CallbackInfoReturnable<Boolean> cir) {
        final BlockModification data = new BlockModification(BlockModificationType.PLACE_FLUID, world, pos, player);

        if (BlockModificationHooks.MODIFY_BLOCK.invoker().onModify(data)) {
            cir.setReturnValue(false);

            if (player instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) player).networkHandler.sendPacket(new BlockUpdateS2CPacket(player.world, pos));
                PlayerUtils.syncPlayerItems(player);
            }
        }
    }
}
