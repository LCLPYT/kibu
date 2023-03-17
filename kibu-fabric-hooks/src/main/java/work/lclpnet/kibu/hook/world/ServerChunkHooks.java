package work.lclpnet.kibu.hook.world;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Hook versions of {@link ServerChunkEvents} from fabric-lifecycle-events.
 */
public class ServerChunkHooks {

    public static final Hook<ServerChunkEvents.Load> CHUNK_LOAD = HookFactory.createArrayBacked(ServerChunkEvents.Load.class, callbacks -> (serverWorld, chunk) -> {
        for (ServerChunkEvents.Load callback : callbacks) {
            callback.onChunkLoad(serverWorld, chunk);
        }
    });

    public static final Hook<ServerChunkEvents.Unload> CHUNK_UNLOAD = HookFactory.createArrayBacked(ServerChunkEvents.Unload.class, callbacks -> (serverWorld, chunk) -> {
        for (ServerChunkEvents.Unload callback : callbacks) {
            callback.onChunkUnload(serverWorld, chunk);
        }
    });

    static {
        ServerChunkEvents.CHUNK_LOAD.register((world, chunk) -> CHUNK_LOAD.invoker().onChunkLoad(world, chunk));
        ServerChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> CHUNK_UNLOAD.invoker().onChunkUnload(world, chunk));
    }
}
