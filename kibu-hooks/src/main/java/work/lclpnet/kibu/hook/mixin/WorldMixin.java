package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.type.CancellableExplosion;
import work.lclpnet.kibu.hook.world.BlockBreakParticleCallback;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

@Mixin(World.class)
public class WorldMixin {

    @Inject(
            method = "createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/World$ExplosionSourceType;ZLnet/minecraft/particle/ParticleEffect;Lnet/minecraft/particle/ParticleEffect;Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/world/explosion/Explosion;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/explosion/Explosion;collectBlocksAndDamageEntities()V"
            ),
            cancellable = true
    )
    public void kibu$onExplode(@Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, boolean particles, ParticleEffect particle, ParticleEffect emitterParticle, RegistryEntry<SoundEvent> soundEvent, CallbackInfoReturnable<Explosion> cir, @Local Explosion explosion) {
        if (!WorldPhysicsHooks.EXPLOSION.invoker().onExplode(entity)) return;

        ((CancellableExplosion) explosion).kibu$setCancelled(true);
        cir.setReturnValue(explosion);
    }

    @WrapOperation(
            method = "breakBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V"
            )
    )
    public void kibu$onBreakBlockParticles(World world, int eventId, BlockPos pos, int rawId, Operation<Void> original) {
        BlockState state = world.getBlockState(pos);

        if (BlockBreakParticleCallback.HOOK.invoker().onSpawnParticles(world, pos, state)) return;

        original.call(world, eventId, pos, rawId);
    }
}
