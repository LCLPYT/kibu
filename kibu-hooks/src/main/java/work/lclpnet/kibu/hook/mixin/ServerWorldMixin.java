package work.lclpnet.kibu.hook.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import work.lclpnet.kibu.hook.type.CancellableExplosion;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(
            method = "createExplosion",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/explosion/Explosion;shouldDestroy()Z"
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void onExplode(Entity entity, DamageSource damageSource, ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, CallbackInfoReturnable<Explosion> cir, Explosion explosion) {
        if (((CancellableExplosion) explosion).kibu$isCancelled())
            cir.setReturnValue(explosion);
    }

    @Redirect(
            method = "tickChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"
            )
    )
    public boolean onFreeze(ServerWorld instance, BlockPos pos, BlockState blockState) {
        if (blockState.isOf(Blocks.SNOW)) {
            if (WorldPhysicsHooks.SNOW_FALL.invoker().onSnowFall(instance, pos))
                return false;
        } else if (blockState.isOf(Blocks.ICE)) {
            if (WorldPhysicsHooks.FREEZE.invoker().onFade(instance, pos))
                return false;
        }
        return instance.setBlockState(pos, blockState);
    }

    @Redirect(
            method = "tickChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;pushEntitiesUpBeforeBlockChange(Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"
            )
    )
    public BlockState onSnowAccumulatePushEntities(BlockState from, BlockState to, WorldAccess world, BlockPos pos) {
        @SuppressWarnings("DataFlowIssue")
        World w = (World) (Object) this;

        // fire snow fall event a second time to determine if entities should be pushed
        if (WorldPhysicsHooks.SNOW_FALL.invoker().onSnowFall(w, pos)) {
            // cancelled, do not push entities and return original block state
            return from;
        }

        return Block.pushEntitiesUpBeforeBlockChange(from, to, world, pos);
    }
}
