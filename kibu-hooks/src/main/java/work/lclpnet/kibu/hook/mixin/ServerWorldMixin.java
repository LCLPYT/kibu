package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.BlockParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.minecraft.world.explosion.ExplosionImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @Inject(
            method = "createExplosion",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/explosion/ExplosionImpl;explode()I"
            ),
            cancellable = true
    )
    public void kibu$onExplode(@Nullable Entity entity, @Nullable DamageSource damageSource,
                               @Nullable ExplosionBehavior behavior, double x, double y, double z, float power,
                               boolean createFire, World.ExplosionSourceType explosionSourceType,
                               ParticleEffect smallParticle, ParticleEffect largeParticle,
                               Pool<BlockParticleEffect> blockParticles, RegistryEntry<SoundEvent> soundEvent,
                               CallbackInfo ci, @Local ExplosionImpl explosion) {
        if (WorldPhysicsHooks.EXPLOSION.invoker().onExplode(explosion)) {
            ci.cancel();
        }
    }

    @Redirect(
            method = "tickIceAndSnow",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"
            )
    )
    public boolean kibu$onFreeze(ServerWorld instance, BlockPos pos, BlockState blockState) {
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
            method = "tickIceAndSnow",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;pushEntitiesUpBeforeBlockChange(Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"
            )
    )
    public BlockState kibu$onSnowAccumulatePushEntities(BlockState from, BlockState to, WorldAccess world, BlockPos pos) {
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
