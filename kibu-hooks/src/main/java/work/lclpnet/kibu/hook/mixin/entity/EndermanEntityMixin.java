package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.EntityTeleportCallback;

@Mixin(EndermanEntity.class)
public class EndermanEntityMixin {

    @Inject(
            method = "teleportTo(DDD)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/EndermanEntity;teleport(DDDZ)Z"
            ),
            cancellable = true
    )
    public void kibu$onTeleport(double _x, double _y, double _z, CallbackInfoReturnable<Boolean> cir, @Local Vec3d finalPos) {
        EndermanEntity self = (EndermanEntity) (Object) this;

        double x = finalPos.x, y = finalPos.y, z = finalPos.z;

        if (EntityTeleportCallback.HOOK.invoker().onTeleport(self, x, y, z)) {
            cir.setReturnValue(false);
        }
    }
}
