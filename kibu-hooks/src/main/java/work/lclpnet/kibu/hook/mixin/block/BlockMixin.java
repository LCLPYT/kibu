package work.lclpnet.kibu.hook.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.world.BlockBreakParticleCallback;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

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
    private static void kibu$onTileDrop(Level world, BlockPos pos, ItemStack stack, CallbackInfo ci) {
        if (world.isClientSide() || stack.isEmpty() || !(world instanceof ServerLevel serverWorld)
            || !serverWorld.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) return;

        if (WorldPhysicsHooks.BLOCK_ITEM_DROP.invoker().onTileDrop(serverWorld, pos, stack)) {
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
    private static void kibu$onTileDrop(Level world, BlockPos pos, Direction direction, ItemStack stack, CallbackInfo ci) {
        if (world.isClientSide() || stack.isEmpty() || !(world instanceof ServerLevel serverWorld)
            || !serverWorld.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) return;

        if (WorldPhysicsHooks.BLOCK_ITEM_DROP.invoker().onTileDrop(serverWorld, pos, stack)) {
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
    public void kibu$onTileXpDrop(ServerLevel world, BlockPos pos, int size, CallbackInfo ci) {
        if (WorldPhysicsHooks.BLOCK_XP_DROP.invoker().onTileDropExperience(world, pos, size)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "spawnDestroyParticles",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onSpawnBreakParticles(Level world, Player player, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (BlockBreakParticleCallback.HOOK.invoker().onSpawnParticles(world, pos, state)) {
            ci.cancel();
        }
    }
}
