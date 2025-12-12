package work.lclpnet.kibu.networking.protocol;

import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.slf4j.Logger;
import work.lclpnet.kibu.hook.player.PlayerConnectionHooks;
import work.lclpnet.kibu.networking.mixin.ServerLoginPacketListenerAccessor;

import java.util.UUID;

public class ServerProtocolHandler {

    private final Protocol protocol;
    private final Logger logger;
    private final Object2IntMap<UUID> playerVersions = new Object2IntOpenHashMap<>();
    private volatile boolean registered = false;

    public ServerProtocolHandler(Protocol protocol, Logger logger) {
        this.protocol = protocol;
        this.logger = logger;
    }

    public void register() {
        if (registered) return;

        synchronized (this) {
            if (registered) return;

            registered = true;
        }

        ServerLoginNetworking.registerGlobalReceiver(protocol.id(), this::receiveLoginVersion);

        ServerLoginConnectionEvents.QUERY_START.register(this::onLoginStart);

        ServerLoginConnectionEvents.DISCONNECT.register((handler, server) -> {
            GameProfile profile = ((ServerLoginPacketListenerAccessor) handler).getAuthenticatedProfile();

            if (profile != null) {
                removePlayer(profile.id());
            }
        });

        PlayerConnectionHooks.QUIT.register(player -> removePlayer(player.getUUID()));

        PlayerConnectionHooks.JOIN.register(this::onPlayerJoin);
    }

    private void onLoginStart(ServerLoginPacketListenerImpl handler, MinecraftServer server, LoginPacketSender sender, ServerLoginNetworking.LoginSynchronizer synchronizer) {
        // send protocol version query
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(protocol.version());

        sender.sendPacket(protocol.id(), buf);
    }

    private void receiveLoginVersion(MinecraftServer server, ServerLoginPacketListenerImpl handler, boolean understood,
                                     FriendlyByteBuf buf, ServerLoginNetworking.LoginSynchronizer synchronizer, PacketSender sender) {
        int version;

        if (understood) {
            // the client understood the protocol version query
            version = buf.readVarInt();
        } else {
            logger.debug("Client {} doesn't support protocol {}", handler.getUserName(), protocol.id());
            return;
        }

        logger.debug("Client {} has protocol version {} of protocol {}", handler.getUserName(), version, protocol.id());

        if (!protocol.supported().test(version)) {
            logger.debug("Protocol {} version of client {} is not supported (client_version={}, server_version={})", protocol.id(), handler.getUserName(), version, protocol.version());
        }

        GameProfile profile = ((ServerLoginPacketListenerAccessor) handler).getAuthenticatedProfile();

        if (profile == null) {
            logger.error("Game profile is not set, but should be initialized by now...");
            return;
        }

        UUID uuid = profile.id();

        synchronized (this) {
            playerVersions.put(uuid, version);
        }
    }

    private void removePlayer(UUID uuid) {
        synchronized (this) {
            playerVersions.removeInt(uuid);
        }
    }

    private void onPlayerJoin(ServerPlayer player) {
        int clientVersion = clientVersion(player);

        if (clientVersion < 0 || protocol.supported().test(clientVersion)) return;

        String path = protocol.id().getNamespace() + "." + protocol.id().getPath().replace('/', '.');
        MutableComponent msg;

        if (protocol.version() >= clientVersion) {
            msg = Component.translatableWithFallback("kibu.proto.%s.too_old".formatted(path),
                    "Your client uses an older version of protocol \"%s\" than the server. Please update, so that everything works properly.",
                    protocol.id().toString());
        } else {
            msg = Component.translatableWithFallback("kibu.proto.%s.too_new".formatted(path),
                    "Your client uses a newer version of protocol \"%s\" than the server. Some things may not work properly.",
                    protocol.id().toString());
        }

        player.sendSystemMessage(msg.withColor(0xeb9605));
    }

    public synchronized int clientVersion(ServerPlayer player) {
        return playerVersions.getOrDefault(player.getUUID(), -1);
    }

    public boolean understands(ServerPlayer player) {
        return protocol.supported().test(clientVersion(player));
    }
}
