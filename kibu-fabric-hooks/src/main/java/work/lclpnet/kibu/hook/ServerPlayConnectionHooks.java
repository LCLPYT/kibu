package work.lclpnet.kibu.hook;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import work.lclpnet.mplugins.hook.Hook;
import work.lclpnet.mplugins.hook.HookFactory;

/**
 * Hook versions of {@link ServerPlayConnectionEvents} from fabric-networking-api.
 */
public class ServerPlayConnectionHooks {

    public static final Hook<ServerPlayConnectionEvents.Init> INIT = HookFactory.createArrayBacked(ServerPlayConnectionEvents.Init.class, callbacks -> (handler, server) -> {
        for (ServerPlayConnectionEvents.Init callback : callbacks) {
            callback.onPlayInit(handler, server);
        }
    });

    public static final Hook<ServerPlayConnectionEvents.Join> JOIN = HookFactory.createArrayBacked(ServerPlayConnectionEvents.Join.class, callbacks -> (handler, sender, server) -> {
        for (ServerPlayConnectionEvents.Join callback : callbacks) {
            callback.onPlayReady(handler, sender, server);
        }
    });

    public static final Hook<ServerPlayConnectionEvents.Disconnect> DISCONNECT = HookFactory.createArrayBacked(ServerPlayConnectionEvents.Disconnect.class, callbacks -> (handler, server) -> {
        for (ServerPlayConnectionEvents.Disconnect callback : callbacks) {
            callback.onPlayDisconnect(handler, server);
        }
    });

    static {
        ServerPlayConnectionEvents.INIT.register((handler, server) -> INIT.invoker().onPlayInit(handler, server));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> JOIN.invoker().onPlayReady(handler, sender, server));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> DISCONNECT.invoker().onPlayDisconnect(handler, server));
    }
}
