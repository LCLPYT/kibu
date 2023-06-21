package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import work.lclpnet.kibu.hook.player.PlayerMountHooks;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(
            method = "startRiding",
            at = @At("RETURN")
    )
    public void kibu$onStartedRiding(Entity vehicle, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        @SuppressWarnings("DataFlowIssue")
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;

        PlayerMountHooks.MOUNTED.invoker().doAfter(self, vehicle);
    }

    @Inject(
            method = "stopRiding",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;stopRiding()V"
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    public void kibu$onStoppedRiding(CallbackInfo ci, Entity vehicle) {
        @SuppressWarnings("DataFlowIssue")
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;

        PlayerMountHooks.DISMOUNTED.invoker().doAfter(self, vehicle);
    }
}
