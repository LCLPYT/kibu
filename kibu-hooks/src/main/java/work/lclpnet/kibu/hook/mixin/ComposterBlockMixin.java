package work.lclpnet.kibu.hook.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.player.PlayerEntity;
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

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {

    @Inject(
            method = "onUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/ComposterBlock;addToComposter(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/block/BlockState;"
            ),
            cancellable = true
    )
    public void onAddToComposter(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (BlockModificationHooks.COMPOSTER.invoker().onModify(world, pos, player)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }

    @Inject(
            method = "onUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/ComposterBlock;emptyFullComposter(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"
            ),
            cancellable = true
    )
    public void onEmptyComposter(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (BlockModificationHooks.COMPOSTER.invoker().onModify(world, pos, player)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
