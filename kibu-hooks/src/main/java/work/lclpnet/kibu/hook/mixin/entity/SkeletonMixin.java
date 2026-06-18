package work.lclpnet.kibu.hook.mixin.entity;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.EntityConvertCallback;

@Mixin(Skeleton.class)
public class SkeletonMixin {

    @Inject(
            method = "doFreezeConversion",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onConvertToStray(CallbackInfo ci) {
        Skeleton self = (Skeleton) (Object) this;

        if (EntityConvertCallback.HOOK.invoker().onConvert(self, EntityTypes.STRAY)) {
            ci.cancel();
        }
    }
}
