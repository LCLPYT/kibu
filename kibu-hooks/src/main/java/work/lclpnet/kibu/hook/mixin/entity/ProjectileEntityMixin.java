package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.ProjectileCanHitCallback;
import work.lclpnet.kibu.hook.entity.ProjectileHooks;

@Mixin(ProjectileEntity.class)
public class ProjectileEntityMixin {

    @Inject(
            method = "onBlockHit",
            at = @At("TAIL")
    )
    public void kibu$onBlockHit(BlockHitResult blockHitResult, CallbackInfo ci) {
        ProjectileEntity self = (ProjectileEntity) (Object) this;

        ProjectileHooks.HIT_BLOCK.invoker().onHitBlock(self, blockHitResult);
    }

    @ModifyReturnValue(method = "canHit", at = @At("RETURN"))
    public boolean kibu$canHit(boolean original, @Local(argsOnly = true) Entity entity) {
        if (!original) return false;

        ProjectileEntity self = (ProjectileEntity) (Object) this;

        return ProjectileCanHitCallback.HOOK.invoker().canHit(self, entity);
    }
}
