package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.EntityTeleportCallback;

@Mixin(ShulkerEntity.class)
public class ShulkerEntityMixin {

    @Inject(
            method = "tryTeleport",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/ShulkerEntity;detach()V"
            ),
            cancellable = true
    )
    public void kibu$onTeleport(CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) BlockPos pos) {
        ShulkerEntity shulker = (ShulkerEntity) (Object) this;

        double x = pos.getX() + 0.5, y = pos.getY(), z = pos.getZ() + 0.5;

        if (EntityTeleportCallback.HOOK.invoker().onTeleport(shulker, x, y, z)) {
            cir.setReturnValue(false);
        }
    }
}
