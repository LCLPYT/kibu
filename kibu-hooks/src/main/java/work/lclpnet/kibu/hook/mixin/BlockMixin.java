package work.lclpnet.kibu.hook.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.world.BlockBreakParticleCallback;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(
            method = "dropStack(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;dropStack(Lnet/minecraft/world/World;Ljava/util/function/Supplier;Lnet/minecraft/item/ItemStack;)V"
            ),
            cancellable = true
    )
    private static void kibu$onTileDrop(World world, BlockPos pos, ItemStack stack, CallbackInfo ci) {
        if (world.isClient || stack.isEmpty() || !world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) return;

        if (WorldPhysicsHooks.BLOCK_ITEM_DROP.invoker().onTileDrop(world, pos, stack)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "dropStack(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Lnet/minecraft/item/ItemStack;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;dropStack(Lnet/minecraft/world/World;Ljava/util/function/Supplier;Lnet/minecraft/item/ItemStack;)V"
            ),
            cancellable = true
    )
    private static void kibu$onTileDrop(World world, BlockPos pos, Direction direction, ItemStack stack, CallbackInfo ci) {
        if (world.isClient || stack.isEmpty() || !world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) return;

        if (WorldPhysicsHooks.BLOCK_ITEM_DROP.invoker().onTileDrop(world, pos, stack)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "dropExperience",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/ExperienceOrbEntity;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;I)V"
            ),
            cancellable = true
    )
    public void kibu$onTileXpDrop(ServerWorld world, BlockPos pos, int size, CallbackInfo ci) {
        if (WorldPhysicsHooks.BLOCK_XP_DROP.invoker().onTileDropExperience(world, pos, size)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "spawnBreakParticles",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onSpawnBreakParticles(World world, PlayerEntity player, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (BlockBreakParticleCallback.HOOK.invoker().onSpawnParticles(world, pos, state)) {
            ci.cancel();
        }
    }
}
