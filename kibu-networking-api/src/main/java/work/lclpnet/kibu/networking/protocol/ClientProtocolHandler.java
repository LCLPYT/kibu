package work.lclpnet.kibu.networking.protocol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.PacketCallbacks;
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

    private CompletableFuture<PacketByteBuf> onQueryVersion(MinecraftClient client, ClientLoginNetworkHandler handler, PacketByteBuf buf, Consumer<PacketCallbacks> callbacksConsumer) {
        serverVersion = buf.readVarInt();
        understands = protocol.supported().test(serverVersion);

        logger.info("Server uses protocol {}: server_version={}, client_version={}, supported={}", protocol.id(), serverVersion, protocol.version(), protocol.supported().test(serverVersion));

        PacketByteBuf response = PacketByteBufs.create();
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
