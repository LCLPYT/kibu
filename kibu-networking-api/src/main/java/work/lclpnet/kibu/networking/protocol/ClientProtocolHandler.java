package work.lclpnet.kibu.networking.protocol;

import io.netty.channel.ChannelFutureListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.FriendlyByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.FriendlyByteBuf;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ClientProtocolHandler {

    private final Protocol protocol;
    private final Logger logger;
    private volatile boolean registered = false;
    private int serverVersion = -1;
    private boolean understands = false;

    public ClientProtocolHandler(Protocol protocol, Logger logger) {
        this.protocol = protocol;
        this.logger = logger;
    }

    public void register() {
        if (registered) return;

        synchronized (this) {
            if (registered) return;

            registered = true;
        }

        ClientLoginNetworking.registerGlobalReceiver(protocol.id(), this::onQueryVersion);
    }

    private CompletableFuture<FriendlyByteBuf> onQueryVersion(Minecraft client, ClientHandshakePacketListenerImpl handler, FriendlyByteBuf buf, Consumer<ChannelFutureListener> callbacksConsumer) {
        serverVersion = buf.readVarInt();
        understands = protocol.supported().test(serverVersion);

        logger.info("Server uses protocol {}: server_version={}, client_version={}, supported={}", protocol.id(), serverVersion, protocol.version(), protocol.supported().test(serverVersion));

        FriendlyByteBuf response = FriendlyByteBufs.create();
        response.writeVarInt(protocol.version());

        return CompletableFuture.completedFuture(response);
    }

    public int serverVersion() {
        return serverVersion;
    }

    public boolean understands() {
        return understands;
    }
}
