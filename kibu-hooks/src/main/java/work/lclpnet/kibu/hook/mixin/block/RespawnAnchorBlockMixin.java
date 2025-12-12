package work.lclpnet.kibu.hook.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.player.PlayerSpawnPointChangeCallback;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;

@Mixin(RespawnAnchorBlock.class)
public class RespawnAnchorBlockMixin {

    @Inject(
            method = "useItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/RespawnAnchorBlock;charge(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"
            ),
            cancellable = true
    )
    public void kibu$onCharge(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (BlockModificationHooks.CHARGE_RESPAWN_ANCHOR.invoker().onModify(world, pos, player)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(
            method = "useWithoutItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/RespawnAnchorBlock;explode(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"
            ),
            cancellable = true
    )
    public void kibu$onExplode(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (BlockModificationHooks.EXPLODE_RESPAWN_LOCATION.invoker().onModify(world, pos, player)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(
            method = "useWithoutItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;setRespawnPosition(Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;Z)V"
            ),
            cancellable = true
    )
    public void kibu$onSetSpawnPoint(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (PlayerSpawnPointChangeCallback.HOOK.invoker().onChange(player, world, pos)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
