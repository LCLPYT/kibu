package work.lclpnet.kibu.hook.mixin.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Skeleton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.EntityConvertCallback;

@Mixin(Skeleton.class)
public class SkeletonEntityMixin {

    @Inject(
            method = "doFreezeConversion",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onConvertToStray(CallbackInfo ci) {
        Skeleton self = (Skeleton) (Object) this;

        if (EntityConvertCallback.HOOK.invoker().onConvert(self, EntityType.STRAY)) {
            ci.cancel();
        }
    }
}
