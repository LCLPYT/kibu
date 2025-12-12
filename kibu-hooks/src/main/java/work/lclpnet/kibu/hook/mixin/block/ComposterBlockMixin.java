package work.lclpnet.kibu.hook.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.util.MixinUtils;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {

    @Inject(
            method = "useItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/ComposterBlock;addItem(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/level/block/state/BlockState;"
            ),
            cancellable = true
    )
    public void kibu$onAddToComposter(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (BlockModificationHooks.COMPOSTER.invoker().onModify(world, pos, player)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(
            method = "useWithoutItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/ComposterBlock;extractProduce(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
            ),
            cancellable = true
    )
    public void kibu$onEmptyComposter(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (BlockModificationHooks.COMPOSTER.invoker().onModify(world, pos, player)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @WrapOperation(
            method = "extractProduce",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
            )
    )
    private static boolean kibu$onDropItem(Level world, Entity entity, Operation<Boolean> original, @Local(argsOnly = true) BlockPos pos) {
        return MixinUtils.wrapBlockItemDrop(world, entity, original, pos);
    }
}
