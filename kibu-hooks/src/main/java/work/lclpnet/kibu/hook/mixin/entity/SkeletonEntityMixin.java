package work.lclpnet.kibu.hook.mixin.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SkeletonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.EntityConvertCallback;

@Mixin(SkeletonEntity.class)
public class SkeletonEntityMixin {

    @Inject(
            method = "convertToStray",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onConvertToStray(CallbackInfo ci) {
        SkeletonEntity self = (SkeletonEntity) (Object) this;

        if (EntityConvertCallback.HOOK.invoker().onConvert(self, EntityType.STRAY)) {
            ci.cancel();
        }
    }
}
