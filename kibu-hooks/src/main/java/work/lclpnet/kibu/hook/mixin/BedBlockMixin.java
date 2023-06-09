package work.lclpnet.kibu.hook.mixin;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
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
import work.lclpnet.kibu.hook.player.PlayerSpawnPointChangeCallback;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;

@Mixin(BedBlock.class)
public class BedBlockMixin {

    @Inject(
            method = "onUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z",
                    ordinal = 0
            ),
            cancellable = true
    )
    public void kibu$onExplosion(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (BlockModificationHooks.EXPLODE_RESPAWN_LOCATION.invoker().onModify(world, pos, player)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }

    @Inject(
            method = "onUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;trySleep(Lnet/minecraft/util/math/BlockPos;)Lcom/mojang/datafixers/util/Either;"
            ),
            cancellable = true
    )
    public void kibu$onTrySleep(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (PlayerSpawnPointChangeCallback.HOOK.invoker().onChange(player, world, pos)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
