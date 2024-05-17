package work.lclpnet.kibu.hook.mixin;

import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.network.ServerSendPacketCallback;

@Mixin(ServerCommonNetworkHandler.class)
public class ServerCommonNetworkHandlerMixin {

    @Inject(
            method = "send",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$send(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
        ServerCommonNetworkHandler self = (ServerCommonNetworkHandler) (Object) this;

        if (ServerSendPacketCallback.HOOK.invoker().shouldRetainPacket(packet, self)) {
            ci.cancel();
        }
    }
}
