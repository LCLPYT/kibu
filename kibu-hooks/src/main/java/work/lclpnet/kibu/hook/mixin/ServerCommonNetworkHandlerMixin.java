package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.netty.channel.ChannelFutureListener;
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
    public void kibu$send(Packet<?> packet, ChannelFutureListener channelFutureListener, CallbackInfo ci,
                          @Local(argsOnly = true) LocalRef<Packet<?>> capture) {

        ServerCommonNetworkHandler self = (ServerCommonNetworkHandler) (Object) this;

        var res = ServerSendPacketCallback.HOOK.invoker().overridePacket(packet, self);

        if (res.isPass()) return;

        var modified = res.get();

        if (modified.isPresent()) {
            capture.set(modified.get());
        } else {
            ci.cancel();
        }
    }
}
