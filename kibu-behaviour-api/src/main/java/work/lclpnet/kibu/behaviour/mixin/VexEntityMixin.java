package work.lclpnet.kibu.behaviour.mixin;

import net.minecraft.world.entity.monster.Vex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.behaviour.type.KibuVexEntity;

@Mixin(Vex.class)
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
                    target = "Lnet/minecraft/world/entity/monster/Monster;tick()V"
            )
    )
    public void kibu$beforeTick(CallbackInfo ci) {
        if (!forceClip) return;

        Vex vex = (Vex) (Object) this;

        vex.noPhysics = false;
    }
}
