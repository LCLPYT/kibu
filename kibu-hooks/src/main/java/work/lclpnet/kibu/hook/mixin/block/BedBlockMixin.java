package work.lclpnet.kibu.hook.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.level.BlockModificationHooks;
import work.lclpnet.kibu.hook.player.PlayerSpawnPointChangeCallback;

@Mixin(BedBlock.class)
public class BedBlockMixin {

    @Inject(
            method = "useWithoutItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z",
                    ordinal = 0
            ),
            cancellable = true
    )
    public void kibu$onExplosion(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (BlockModificationHooks.EXPLODE_RESPAWN_LOCATION.invoker().onModify(level, pos, player)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(
            method = "useWithoutItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;startSleepInBed(Lnet/minecraft/core/BlockPos;)Lcom/mojang/datafixers/util/Either;"
            ),
            cancellable = true
    )
    public void kibu$onTrySleep(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (PlayerSpawnPointChangeCallback.HOOK.invoker().onChange(player, level, pos)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
