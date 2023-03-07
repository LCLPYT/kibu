package work.lclpnet.kibu.hook.entity;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import work.lclpnet.mplugins.hook.Hook;
import work.lclpnet.mplugins.hook.HookFactory;

/**
 * Hook versions of {@link ServerEntityCombatEvents} from fabric-entity-events.
 */
public class ServerEntityCombatHooks {

    public static final Hook<ServerEntityCombatEvents.AfterKilledOtherEntity> AFTER_KILLED_OTHER_ENTITY = HookFactory.createArrayBacked(ServerEntityCombatEvents.AfterKilledOtherEntity.class,
            callbacks -> (world, entity, killedEntity) -> {
                for (ServerEntityCombatEvents.AfterKilledOtherEntity callback : callbacks) {
                    callback.afterKilledOtherEntity(world, entity, killedEntity);
                }
            });

    static {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> AFTER_KILLED_OTHER_ENTITY.invoker().afterKilledOtherEntity(world, entity, killedEntity));
    }
}
