package work.lclpnet.kibu.hook.level;

import net.minecraft.server.MinecraftServer;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Called when the server is about to unload the main world.
 */
public interface ServerWorldUnreadyCallback {

    Hook<ServerWorldUnreadyCallback> HOOK = HookFactory.createArrayBacked(ServerWorldUnreadyCallback.class, callbacks -> (server) -> {
        for (ServerWorldUnreadyCallback callback : callbacks) {
            callback.onWorldUnready(server);
        }
    });

    void onWorldUnready(MinecraftServer server);
}
