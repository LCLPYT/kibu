package work.lclpnet.kibu.hook.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

    @Shadow @Final private Fluid fluid;

    @Inject(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/FluidDrainable;tryDrainFluid(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/item/ItemStack;"
            ),
            cancellable = true
    )
    public void kibu$onPickupFluid(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir,
                                   @Local(ordinal = 0) BlockPos pos) {

        if (BlockModificationHooks.PICKUP_FLUID.invoker().onTransfer(world, pos, player, fluid)) {
            cir.setReturnValue(ActionResult.FAIL);

            if (player instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) player).networkHandler.sendPacket(new BlockUpdateS2CPacket(world, pos));
                PlayerUtils.syncPlayerItems(player);
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
    public void kibu$onPlaceFluid(LivingEntity user, World world, BlockPos pos, BlockHitResult hitResult, CallbackInfoReturnable<Boolean> cir) {
        if (BlockModificationHooks.PLACE_FLUID.invoker().onTransfer(world, pos, user, fluid)) {
            cir.setReturnValue(false);

            if (user instanceof ServerPlayerEntity player) {
                player.networkHandler.sendPacket(new BlockUpdateS2CPacket(player.getWorld(), pos));
                PlayerUtils.syncPlayerItems(player);
            }
        }
    }
}
