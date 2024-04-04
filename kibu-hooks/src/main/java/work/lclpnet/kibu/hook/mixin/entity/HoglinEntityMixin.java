package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HoglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.entity.EntityConvertCallback;

@Mixin(HoglinEntity.class)
public class HoglinEntityMixin {

    @Shadow private int timeInOverworld;

    @ModifyExpressionValue(
            method = "mobTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/HoglinEntity;canConvert()Z"
            )
    )
    public boolean kibu$onConvert(boolean original) {
        if (!original || timeInOverworld < 300) return original;

        // the hoglin would convert
        HoglinEntity self = (HoglinEntity) (Object) this;

        return !EntityConvertCallback.HOOK.invoker().onConvert(self, EntityType.ZOGLIN);
    }
}
