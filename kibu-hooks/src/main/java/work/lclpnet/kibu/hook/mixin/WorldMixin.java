package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import work.lclpnet.kibu.hook.model.CancellableExplosion;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

@Mixin(World.class)
public class WorldMixin {

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(
            method = "createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/World$ExplosionSourceType;Z)Lnet/minecraft/world/explosion/Explosion;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/explosion/Explosion;collectBlocksAndDamageEntities()V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    public void onExplode(Entity entity, DamageSource damageSource, ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, boolean particles, CallbackInfoReturnable<Explosion> cir, Explosion.DestructionType destructionType, Explosion explosion) {
        if (!WorldPhysicsHooks.EXPLOSION.invoker().onExplode(entity)) return;

        ((CancellableExplosion) explosion).kibu$setCancelled(true);
        cir.setReturnValue(explosion);
    }
}
