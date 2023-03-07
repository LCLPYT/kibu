package work.lclpnet.kibu.hook;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import work.lclpnet.mplugins.hook.Hook;
import work.lclpnet.mplugins.hook.HookFactory;

/**
 * Hook versions of {@link ServerTickEvents} from fabric-lifecycle-events.
 */
public class ServerTickHooks {

    public static final Hook<ServerTickEvents.StartTick> START_SERVER_TICK = HookFactory.createArrayBacked(ServerTickEvents.StartTick.class,
            callbacks -> server -> {
                for (ServerTickEvents.StartTick event : callbacks) {
                    event.onStartTick(server);
                }
            });

    public static final Hook<ServerTickEvents.EndTick> END_SERVER_TICK = HookFactory.createArrayBacked(ServerTickEvents.EndTick.class,
            callbacks -> server -> {
                for (ServerTickEvents.EndTick event : callbacks) {
                    event.onEndTick(server);
                }
            });

    public static final Hook<ServerTickEvents.StartWorldTick> START_WORLD_TICK = HookFactory.createArrayBacked(ServerTickEvents.StartWorldTick.class,
            callbacks -> world -> {
                for (ServerTickEvents.StartWorldTick callback : callbacks) {
                    callback.onStartTick(world);
                }
            });

    public static final Hook<ServerTickEvents.EndWorldTick> END_WORLD_TICK = HookFactory.createArrayBacked(ServerTickEvents.EndWorldTick.class,
            callbacks -> world -> {
                for (ServerTickEvents.EndWorldTick callback : callbacks) {
                    callback.onEndTick(world);
                }
            });

    static {
        ServerTickEvents.START_SERVER_TICK.register(server -> START_SERVER_TICK.invoker().onStartTick(server));
        ServerTickEvents.END_SERVER_TICK.register(server -> END_SERVER_TICK.invoker().onEndTick(server));
        ServerTickEvents.START_WORLD_TICK.register(world -> START_WORLD_TICK.invoker().onStartTick(world));
        ServerTickEvents.END_WORLD_TICK.register(world -> END_WORLD_TICK.invoker().onEndTick(world));
    }
}
