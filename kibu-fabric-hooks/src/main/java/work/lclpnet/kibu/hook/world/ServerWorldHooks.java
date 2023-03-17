package work.lclpnet.kibu.hook.world;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Hook versions of {@link ServerWorldEvents} from fabric-lifecycle-events.
 */
public class ServerWorldHooks {

    public static final Hook<ServerWorldEvents.Load> LOAD = HookFactory.createArrayBacked(ServerWorldEvents.Load.class,
            callbacks -> (server, world) -> {
                for (ServerWorldEvents.Load callback : callbacks) {
                    callback.onWorldLoad(server, world);
                }
            });

    public static final Hook<ServerWorldEvents.Unload> UNLOAD = HookFactory.createArrayBacked(ServerWorldEvents.Unload.class,
            callbacks -> (server, world) -> {
                for (ServerWorldEvents.Unload callback : callbacks) {
                    callback.onWorldUnload(server, world);
                }
            });

    static {
        ServerWorldEvents.LOAD.register((server, world) -> LOAD.invoker().onWorldLoad(server, world));
        ServerWorldEvents.UNLOAD.register((server, world) -> UNLOAD.invoker().onWorldUnload(server, world));
    }
}
