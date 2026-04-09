package work.lclpnet.kibu.hook.entity;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Hook versions of {@link ServerEntityLevelChangeEvents} from fabric-entity-events.
 */
public class ServerEntityLevelChangeHooks {

    public static final Hook<ServerEntityLevelChangeEvents.AfterEntityChange> AFTER_ENTITY_CHANGE_LEVEL = HookFactory.createArrayBacked(ServerEntityLevelChangeEvents.AfterEntityChange.class,
            callbacks -> (originalEntity, newEntity, origin, destination) -> {
                for (ServerEntityLevelChangeEvents.AfterEntityChange callback : callbacks) {
                    callback.afterChangeLevel(originalEntity, newEntity, origin, destination);
                }
            });

    public static final Hook<ServerEntityLevelChangeEvents.AfterPlayerChange> AFTER_PLAYER_CHANGE_LEVEL = HookFactory.createArrayBacked(ServerEntityLevelChangeEvents.AfterPlayerChange.class,
            callbacks -> (player, origin, destination) -> {
                for (ServerEntityLevelChangeEvents.AfterPlayerChange callback : callbacks) {
                    callback.afterChangeLevel(player, origin, destination);
                }
            });

    static {
        ServerEntityLevelChangeEvents.AFTER_ENTITY_CHANGE_LEVEL.register((originalEntity, newEntity, origin, destination) -> AFTER_ENTITY_CHANGE_LEVEL.invoker().afterChangeLevel(originalEntity, newEntity, origin, destination));
        ServerEntityLevelChangeEvents.AFTER_PLAYER_CHANGE_LEVEL.register((player, origin, destination) -> AFTER_PLAYER_CHANGE_LEVEL.invoker().afterChangeLevel(player, origin, destination));
    }
}
