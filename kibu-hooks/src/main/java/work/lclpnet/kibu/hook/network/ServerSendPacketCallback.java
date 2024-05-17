package work.lclpnet.kibu.hook.network;

import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface ServerSendPacketCallback {

    Hook<ServerSendPacketCallback> HOOK = HookFactory.createArrayBacked(ServerSendPacketCallback.class, callbacks -> (packet, handler) -> {
        boolean retain = false;

        for (ServerSendPacketCallback callback : callbacks) {
            if (callback.shouldRetainPacket(packet, handler)) {
                retain = true;
            }
        }

        return retain;
    });

    boolean shouldRetainPacket(Packet<?> packet, ServerCommonNetworkHandler handler);
}
