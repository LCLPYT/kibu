package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.network.CustomClickActionCallback;
import work.lclpnet.kibu.hook.network.ServerSendPacketCallback;

@Mixin(ServerCommonPacketListenerImpl.class)
public class ServerCommonPacketListenerMixin {

    @Inject(
            method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$send(Packet<?> packet, ChannelFutureListener channelFutureListener, CallbackInfo ci,
                          @Local(argsOnly = true, name = "packet") LocalRef<Packet<?>> capture) {

        ServerCommonPacketListenerImpl self = (ServerCommonPacketListenerImpl) (Object) this;

        var res = ServerSendPacketCallback.HOOK.invoker().overridePacket(packet, self);

        if (res.isPass()) return;

        var modified = res.get();

        if (modified.isPresent()) {
            capture.set(modified.get());
        } else {
            ci.cancel();
        }
    }

    @Inject(
            method = "handleCustomClickAction",
            at = @At("TAIL")
    )
    public void ap2$onCustomClickAction(ServerboundCustomClickActionPacket packet, CallbackInfo ci) {
        if ((Object) this instanceof ServerGamePacketListenerImpl handler) {
            ServerPlayer player = handler.player;

            if (player == null) return;

            CustomClickActionCallback.HOOK.invoker().onCustomClickAction(player, packet.id(), packet.payload());
        }
    }
}
