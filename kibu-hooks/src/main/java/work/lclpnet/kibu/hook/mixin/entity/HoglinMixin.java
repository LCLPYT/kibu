package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.entity.EntityConvertCallback;

@Mixin(Hoglin.class)
public class HoglinMixin {

    @Shadow private int timeInOverworld;

    @ModifyExpressionValue(
            method = "customServerAiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/hoglin/Hoglin;isConverting()Z"
            )
    )
    public boolean kibu$onConvert(boolean original) {
        if (!original || timeInOverworld < 300) return original;

        // the hoglin would convert
        Hoglin self = (Hoglin) (Object) this;

        return !EntityConvertCallback.HOOK.invoker().onConvert(self, EntityType.ZOGLIN);
    }
}
