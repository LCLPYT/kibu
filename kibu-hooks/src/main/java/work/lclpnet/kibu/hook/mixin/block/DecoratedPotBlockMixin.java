package work.lclpnet.kibu.hook.mixin.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
            method = "onUse",
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/entity/DecoratedPotBlockEntity;wobble(Lnet/minecraft/block/entity/DecoratedPotBlockEntity$WobbleType;)V",
                    ordinal = 0
            ), @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V",
                    ordinal = 1
            )},
            cancellable = true
    )
    public void kibu$beforeWobble(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (BlockModificationHooks.DECORATIVE_POT_STORE.invoker().onModify(world, hit.getBlockPos(), player)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }

    @Inject(
            method = "onProjectileHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"
            ),
            cancellable = true
    )
    public void kibu$beforeProjectileDestroy(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile, CallbackInfo ci) {
        if (ProjectileHooks.BREAK_DECORATED_POT.invoker().onAffect(projectile, hit)) {
            ci.cancel();
        }
    }
}
