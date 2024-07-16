package work.lclpnet.kibu.hook.world;

import net.minecraft.server.MinecraftServer;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Called when the server loaded the main world.
 */
public interface ServerWorldReadyCallback {

    Hook<ServerWorldReadyCallback> HOOK = HookFactory.createArrayBacked(ServerWorldReadyCallback.class, callbacks -> (server) -> {
        for (ServerWorldReadyCallback callback : callbacks) {
            callback.onWorldReady(server);
        }
    });

    void onWorldReady(MinecraftServer server);
}
