package work.lclpnet.kibu.hook.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.ProjectileHooks;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;

@Mixin(DecoratedPotBlock.class)
public class DecoratedPotBlockMixin {

    @Inject(
            method = "useItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/DecoratedPotBlockEntity;wobble(Lnet/minecraft/world/level/block/entity/DecoratedPotBlockEntity$WobbleStyle;)V"
            ),
            cancellable = true
    )
    public void kibu$beforeWobble(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (BlockModificationHooks.DECORATIVE_POT_STORE.invoker().onModify(world, hit.getBlockPos(), player)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(
            method = "useWithoutItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"
            ),
            cancellable = true
    )
    public void kibu$beforeWobbleBack(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (BlockModificationHooks.DECORATIVE_POT_STORE.invoker().onModify(world, hit.getBlockPos(), player)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(
            method = "onProjectileHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
            ),
            cancellable = true
    )
    public void kibu$beforeProjectileDestroy(Level world, BlockState state, BlockHitResult hit, Projectile projectile, CallbackInfo ci) {
        if (ProjectileHooks.BREAK_DECORATED_POT.invoker().onAffect(projectile, hit)) {
            ci.cancel();
        }
    }
}
