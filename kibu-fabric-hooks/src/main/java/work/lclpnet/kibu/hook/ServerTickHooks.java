package work.lclpnet.kibu.hook;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

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

    public static final Hook<ServerTickEvents.StartLevelTick> START_LEVEL_TICK = HookFactory.createArrayBacked(ServerTickEvents.StartLevelTick.class,
            callbacks -> level -> {
                for (ServerTickEvents.StartLevelTick callback : callbacks) {
                    callback.onStartTick(level);
                }
            });

    public static final Hook<ServerTickEvents.EndLevelTick> END_LEVEL_TICK = HookFactory.createArrayBacked(ServerTickEvents.EndLevelTick.class,
            callbacks -> level -> {
                for (ServerTickEvents.EndLevelTick callback : callbacks) {
                    callback.onEndTick(level);
                }
            });

    static {
        ServerTickEvents.START_SERVER_TICK.register(server -> START_SERVER_TICK.invoker().onStartTick(server));
        ServerTickEvents.END_SERVER_TICK.register(server -> END_SERVER_TICK.invoker().onEndTick(server));
        ServerTickEvents.START_LEVEL_TICK.register(level -> START_LEVEL_TICK.invoker().onStartTick(level));
        ServerTickEvents.END_LEVEL_TICK.register(level -> END_LEVEL_TICK.invoker().onEndTick(level));
    }
}
