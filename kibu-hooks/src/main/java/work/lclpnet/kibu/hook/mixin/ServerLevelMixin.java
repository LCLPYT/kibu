package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    @Inject(
            method = "explode",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/ServerExplosion;explode()I"
            ),
            cancellable = true
    )
    public void kibu$onExplode(
            @Nullable Entity source,
            @Nullable DamageSource damageSource,
            @Nullable ExplosionDamageCalculator damageCalculator,
            double x,
            double y,
            double z,
            float r,
            boolean fire,
            Level.ExplosionInteraction interactionType,
            ParticleOptions smallExplosionParticles,
            ParticleOptions largeExplosionParticles,
            WeightedList<ExplosionParticleInfo> blockParticles,
            Holder<SoundEvent> explosionSound,
            CallbackInfo ci,
            @Local(name = "explosion") ServerExplosion explosion
    ) {
        if (WorldPhysicsHooks.EXPLOSION.invoker().onExplode(explosion)) {
            ci.cancel();
        }
    }

    @Redirect(
            method = "tickPrecipitation",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"
            )
    )
    public boolean kibu$onFreeze(ServerLevel instance, BlockPos pos, BlockState blockState) {
        if (blockState.is(Blocks.SNOW)) {
            if (WorldPhysicsHooks.SNOW_FALL.invoker().onSnowFall(instance, pos))
                return false;
        } else if (blockState.is(Blocks.ICE)) {
            if (WorldPhysicsHooks.FREEZE.invoker().onFade(instance, pos))
                return false;
        }
        return instance.setBlockAndUpdate(pos, blockState);
    }

    @Redirect(
            method = "tickPrecipitation",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;pushEntitiesUp(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    public BlockState kibu$onSnowAccumulatePushEntities(BlockState state, BlockState newState, LevelAccessor level, BlockPos pos) {
        @SuppressWarnings("DataFlowIssue")
        Level w = (Level) (Object) this;

        // fire snow fall event a second time to determine if entities should be pushed
        if (WorldPhysicsHooks.SNOW_FALL.invoker().onSnowFall(w, pos)) {
            // canceled, do not push entities and return original block state
            return state;
        }

        return Block.pushEntitiesUp(state, newState, level, pos);
    }
}
