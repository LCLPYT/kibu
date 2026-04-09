package work.lclpnet.kibu.hook.level;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Hook versions of {@link ServerLevelEvents} from fabric-lifecycle-events.
 */
public class ServerLevelHooks {

    public static final Hook<ServerLevelEvents.Load> LOAD = HookFactory.createArrayBacked(ServerLevelEvents.Load.class,
            callbacks -> (server, level) -> {
                for (ServerLevelEvents.Load callback : callbacks) {
                    callback.onLevelLoad(server, level);
                }
            });

    public static final Hook<ServerLevelEvents.Unload> UNLOAD = HookFactory.createArrayBacked(ServerLevelEvents.Unload.class,
            callbacks -> (server, level) -> {
                for (ServerLevelEvents.Unload callback : callbacks) {
                    callback.onLevelUnload(server, level);
                }
            });

    static {
        ServerLevelEvents.LOAD.register((server, level) -> LOAD.invoker().onLevelLoad(server, level));
        ServerLevelEvents.UNLOAD.register((server, level) -> UNLOAD.invoker().onLevelUnload(server, level));
    }
}
