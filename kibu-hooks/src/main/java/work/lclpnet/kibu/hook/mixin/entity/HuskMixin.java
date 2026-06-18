package work.lclpnet.kibu.hook.mixin.entity;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.zombie.Husk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.EntityConvertCallback;

@Mixin(Husk.class)
public class HuskMixin {

    @Inject(
            method = "doUnderWaterConversion",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onConvertToZombie(CallbackInfo ci) {
        Husk self = (Husk) (Object) this;

        if (EntityConvertCallback.HOOK.invoker().onConvert(self, EntityTypes.ZOMBIE)) {
            ci.cancel();
        }
    }
}
