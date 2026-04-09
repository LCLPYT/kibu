package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.ProjectileCanHitCallback;
import work.lclpnet.kibu.hook.entity.ProjectileHooks;

@Mixin(Projectile.class)
public class ProjectileMixin {

    @Inject(
            method = "onHitBlock",
            at = @At("TAIL")
    )
    public void kibu$onBlockHit(BlockHitResult hitResult, CallbackInfo ci) {
        Projectile self = (Projectile) (Object) this;

        ProjectileHooks.HIT_BLOCK.invoker().onHitBlock(self, hitResult);
    }

    @ModifyReturnValue(method = "canHitEntity(Lnet/minecraft/world/entity/Entity;)Z", at = @At("RETURN"))
    public boolean kibu$canHit(boolean original, @Local(argsOnly = true, name = "entity") Entity entity) {
        if (!original) return false;

        Projectile self = (Projectile) (Object) this;

        return ProjectileCanHitCallback.HOOK.invoker().canHit(self, entity);
    }
}
