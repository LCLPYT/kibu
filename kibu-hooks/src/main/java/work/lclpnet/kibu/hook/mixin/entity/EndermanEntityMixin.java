package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.EntityTeleportCallback;

@Mixin(EnderMan.class)
public class EndermanEntityMixin {

    @Inject(
            method = "teleport(DDD)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/EnderMan;randomTeleport(DDDZ)Z"
            ),
            cancellable = true
    )
    public void kibu$onTeleport(double _x, double _y, double _z, CallbackInfoReturnable<Boolean> cir, @Local Vec3 finalPos) {
        EnderMan self = (EnderMan) (Object) this;

        double x = finalPos.x, y = finalPos.y, z = finalPos.z;

        if (EntityTeleportCallback.HOOK.invoker().onTeleport(self, x, y, z)) {
            cir.setReturnValue(false);
        }
    }
}
