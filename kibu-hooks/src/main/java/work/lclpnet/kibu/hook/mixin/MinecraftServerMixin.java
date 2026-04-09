package work.lclpnet.kibu.hook.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.level.ServerWorldUnreadyCallback;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerConnectionListener;stop()V"
            ),
            method = "stopServer"
    )
    private void kibu$beforeGetNetworkIo(CallbackInfo ci) {
        MinecraftServer self = (MinecraftServer) (Object) this;
        ServerWorldUnreadyCallback.HOOK.invoker().onWorldUnready(self);
    }
}
