package work.lclpnet.kibu.hook.entity;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Hook versions of {@link ServerPlayerEvents} from fabric-entity-events.
 */
public class ServerPlayerHooks {

    public static final Hook<ServerPlayerEvents.CopyFrom> COPY_FROM = HookFactory.createArrayBacked(ServerPlayerEvents.CopyFrom.class,
            callbacks -> (oldPlayer, newPlayer, alive) -> {
                for (ServerPlayerEvents.CopyFrom callback : callbacks) {
                    callback.copyFromPlayer(oldPlayer, newPlayer, alive);
                }
            });

    public static final Event<ServerPlayerEvents.AfterRespawn> AFTER_RESPAWN = EventFactory.createArrayBacked(ServerPlayerEvents.AfterRespawn.class,
            callbacks -> (oldPlayer, newPlayer, alive) -> {
                for (ServerPlayerEvents.AfterRespawn callback : callbacks) {
                    callback.afterRespawn(oldPlayer, newPlayer, alive);
                }
            });

    static {
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> COPY_FROM.invoker().copyFromPlayer(oldPlayer, newPlayer, alive));
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> AFTER_RESPAWN.invoker().afterRespawn(oldPlayer, newPlayer, alive));
    }
}
