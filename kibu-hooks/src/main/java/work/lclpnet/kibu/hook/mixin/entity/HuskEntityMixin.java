package work.lclpnet.kibu.hook.mixin.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HuskEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.EntityConvertCallback;

@Mixin(HuskEntity.class)
public class HuskEntityMixin {

    @Inject(
            method = "convertInWater",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onConvertToZombie(CallbackInfo ci) {
        HuskEntity self = (HuskEntity) (Object) this;

        if (EntityConvertCallback.HOOK.invoker().onConvert(self, EntityType.ZOMBIE)) {
            ci.cancel();
        }
    }
}
