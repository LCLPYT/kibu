package work.lclpnet.kibu.hook.world;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Hook versions of {@link ServerLifecycleEvents} from fabric-lifecycle-events.
 */
public class ServerLifecycleHooks {

    public static final Hook<ServerLifecycleEvents.StartDataPackReload> START_DATA_PACK_RELOAD = HookFactory.createArrayBacked(ServerLifecycleEvents.StartDataPackReload.class,
            callbacks -> (server, serverResourceManager) -> {
                for (ServerLifecycleEvents.StartDataPackReload callback : callbacks) {
                    callback.startDataPackReload(server, serverResourceManager);
                }
            });

    public static final Hook<ServerLifecycleEvents.EndDataPackReload> END_DATA_PACK_RELOAD = HookFactory.createArrayBacked(ServerLifecycleEvents.EndDataPackReload.class,
            callbacks -> (server, serverResourceManager, success) -> {
                for (ServerLifecycleEvents.EndDataPackReload callback : callbacks) {
                    callback.endDataPackReload(server, serverResourceManager, success);
                }
            });

    static {
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, resourceManager) -> START_DATA_PACK_RELOAD.invoker().startDataPackReload(server, resourceManager));
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> END_DATA_PACK_RELOAD.invoker().endDataPackReload(server, resourceManager, success));
    }
}
