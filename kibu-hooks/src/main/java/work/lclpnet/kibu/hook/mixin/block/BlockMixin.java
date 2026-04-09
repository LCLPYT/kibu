package work.lclpnet.kibu.hook.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.level.BlockBreakParticleCallback;
import work.lclpnet.kibu.hook.level.LevelPhysicsHooks;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(
            method = "popResource(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;popResource(Lnet/minecraft/world/level/Level;Ljava/util/function/Supplier;Lnet/minecraft/world/item/ItemStack;)V"
            ),
            cancellable = true
    )
    private static void kibu$onTileDrop(Level level, BlockPos pos, ItemStack itemStack, CallbackInfo ci) {
        if (level.isClientSide() || itemStack.isEmpty() || !(level instanceof ServerLevel serverWorld)
            || !serverWorld.getGameRules().get(GameRules.BLOCK_DROPS)) return;

        if (LevelPhysicsHooks.BLOCK_ITEM_DROP.invoker().onTileDrop(serverWorld, pos, itemStack)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "popResourceFromFace(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;popResource(Lnet/minecraft/world/level/Level;Ljava/util/function/Supplier;Lnet/minecraft/world/item/ItemStack;)V"
            ),
            cancellable = true
    )
    private static void kibu$onTileDrop(Level level, BlockPos pos, Direction face, ItemStack itemStack, CallbackInfo ci) {
        if (level.isClientSide() || itemStack.isEmpty() || !(level instanceof ServerLevel serverWorld)
            || !serverWorld.getGameRules().get(GameRules.BLOCK_DROPS)) return;

        if (LevelPhysicsHooks.BLOCK_ITEM_DROP.invoker().onTileDrop(serverWorld, pos, itemStack)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "popExperience",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"
            ),
            cancellable = true
    )
    public void kibu$onTileXpDrop(ServerLevel level, BlockPos pos, int amount, CallbackInfo ci) {
        if (LevelPhysicsHooks.BLOCK_XP_DROP.invoker().onTileDropExperience(level, pos, amount)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "spawnDestroyParticles",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onSpawnBreakParticles(Level level, Player player, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (BlockBreakParticleCallback.HOOK.invoker().onSpawnParticles(level, pos, state)) {
            ci.cancel();
        }
    }
}
