package work.lclpnet.kibu.hook;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

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

    public static final Hook<ServerLifecycleEvents.ServerStarting> SERVER_STARTING = HookFactory.createArrayBacked(ServerLifecycleEvents.ServerStarting.class,
            callbacks -> server -> {
                for (var callback : callbacks) {
                    callback.onServerStarting(server);
                }
            });

    public static final Hook<ServerLifecycleEvents.ServerStarted> SERVER_STARTED = HookFactory.createArrayBacked(ServerLifecycleEvents.ServerStarted.class,
            callbacks -> server -> {
                for (var callback : callbacks) {
                    callback.onServerStarted(server);
                }
            });

    public static final Hook<ServerLifecycleEvents.ServerStopping> SERVER_STOPPING = HookFactory.createArrayBacked(ServerLifecycleEvents.ServerStopping.class,
            callbacks -> server -> {
                for (var callback : callbacks) {
                    callback.onServerStopping(server);
                }
            });

    public static final Hook<ServerLifecycleEvents.ServerStopped> SERVER_STOPPED = HookFactory.createArrayBacked(ServerLifecycleEvents.ServerStopped.class,
            callbacks -> server -> {
                for (var callback : callbacks) {
                    callback.onServerStopped(server);
                }
            });

    static {
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, resourceManager) -> START_DATA_PACK_RELOAD.invoker().startDataPackReload(server, resourceManager));
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> END_DATA_PACK_RELOAD.invoker().endDataPackReload(server, resourceManager, success));
        ServerLifecycleEvents.SERVER_STARTING.register(server -> SERVER_STARTING.invoker().onServerStarting(server));
        ServerLifecycleEvents.SERVER_STARTED.register(server -> SERVER_STARTED.invoker().onServerStarted(server));
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> SERVER_STOPPING.invoker().onServerStopping(server));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> SERVER_STOPPED.invoker().onServerStopped(server));
    }
}
