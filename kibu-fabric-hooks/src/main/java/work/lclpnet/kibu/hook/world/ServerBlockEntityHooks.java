package work.lclpnet.kibu.hook.world;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Hook versions of {@link ServerBlockEntityEvents} from fabric-lifecycle-events.
 */
public class ServerBlockEntityHooks {

    public static final Hook<ServerBlockEntityEvents.Load> BLOCK_ENTITY_LOAD = HookFactory.createArrayBacked(ServerBlockEntityEvents.Load.class, callbacks -> (blockEntity, world) -> {
        for (ServerBlockEntityEvents.Load callback : callbacks) {
            callback.onLoad(blockEntity, world);
        }
    });

    public static final Hook<ServerBlockEntityEvents.Unload> BLOCK_ENTITY_UNLOAD = HookFactory.createArrayBacked(ServerBlockEntityEvents.Unload.class, callbacks -> (blockEntity, world) -> {
        for (ServerBlockEntityEvents.Unload callback : callbacks) {
            callback.onUnload(blockEntity, world);
        }
    });

    static {
        ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register((blockEntity, world) -> BLOCK_ENTITY_LOAD.invoker().onLoad(blockEntity, world));
        ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register((blockEntity, world) -> BLOCK_ENTITY_UNLOAD.invoker().onUnload(blockEntity, world));
    }
}
