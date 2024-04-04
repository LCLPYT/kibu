package work.lclpnet.kibu.access.mixin;

import net.minecraft.entity.mob.VexEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.access.type.KibuVexEntity;

@Mixin(VexEntity.class)
public class VexEntityMixin implements KibuVexEntity {

    @Unique
    private boolean forceClip = false;

    @Override
    public void kibu$setForceClipping(boolean noClip) {
        forceClip = noClip;
    }

    @Override
    public boolean kibu$isForceClipping() {
        return forceClip;
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/HostileEntity;tick()V"
            )
    )
    public void kibu$beforeTick(CallbackInfo ci) {
        if (!forceClip) return;

        VexEntity vex = (VexEntity) (Object) this;

        vex.noClip = false;
    }
}
