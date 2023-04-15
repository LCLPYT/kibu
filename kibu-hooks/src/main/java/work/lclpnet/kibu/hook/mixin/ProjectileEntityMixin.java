package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.ProjectileHooks;

@Mixin(ProjectileEntity.class)
public class ProjectileEntityMixin {

    @Inject(
            method = "onBlockHit",
            at = @At("TAIL")
    )
    public void illwalls$onBlockHit(BlockHitResult blockHitResult, CallbackInfo ci) {
        @SuppressWarnings("DataFlowIssue")
        ProjectileEntity self = (ProjectileEntity) (Object) this;

        ProjectileHooks.HIT_BLOCK.invoker().onHitBlock(self, blockHitResult);
    }
}
