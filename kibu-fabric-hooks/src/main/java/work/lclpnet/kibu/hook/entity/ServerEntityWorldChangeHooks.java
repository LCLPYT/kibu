package work.lclpnet.kibu.hook.entity;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Hook versions of {@link ServerEntityWorldChangeEvents} from fabric-entity-events.
 */
public class ServerEntityWorldChangeHooks {

    public static final Hook<ServerEntityWorldChangeEvents.AfterEntityChange> AFTER_ENTITY_CHANGE_WORLD = HookFactory.createArrayBacked(ServerEntityWorldChangeEvents.AfterEntityChange.class,
            callbacks -> (originalEntity, newEntity, origin, destination) -> {
                for (ServerEntityWorldChangeEvents.AfterEntityChange callback : callbacks) {
                    callback.afterChangeWorld(originalEntity, newEntity, origin, destination);
                }
            });

    public static final Hook<ServerEntityWorldChangeEvents.AfterPlayerChange> AFTER_PLAYER_CHANGE_WORLD = HookFactory.createArrayBacked(ServerEntityWorldChangeEvents.AfterPlayerChange.class,
            callbacks -> (player, origin, destination) -> {
                for (ServerEntityWorldChangeEvents.AfterPlayerChange callback : callbacks) {
                    callback.afterChangeWorld(player, origin, destination);
                }
            });

    static {
        ServerEntityWorldChangeEvents.AFTER_ENTITY_CHANGE_WORLD.register((originalEntity, newEntity, origin, destination) -> AFTER_ENTITY_CHANGE_WORLD.invoker().afterChangeWorld(originalEntity, newEntity, origin, destination));
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> AFTER_PLAYER_CHANGE_WORLD.invoker().afterChangeWorld(player, origin, destination));
    }
}
