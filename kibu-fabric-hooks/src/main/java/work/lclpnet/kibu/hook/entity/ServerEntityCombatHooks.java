package work.lclpnet.kibu.hook.entity;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Hook versions of {@link ServerEntityCombatEvents} from fabric-entity-events.
 */
public class ServerEntityCombatHooks {

    public static final Hook<ServerEntityCombatEvents.AfterKilledOtherEntity> AFTER_KILLED_OTHER_ENTITY = HookFactory.createArrayBacked(ServerEntityCombatEvents.AfterKilledOtherEntity.class,
            callbacks -> (world, entity, killedEntity, damageSource) -> {
                for (ServerEntityCombatEvents.AfterKilledOtherEntity callback : callbacks) {
                    callback.afterKilledOtherEntity(world, entity, killedEntity, damageSource);
                }
            });

    static {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity, damageSource) -> AFTER_KILLED_OTHER_ENTITY.invoker().afterKilledOtherEntity(world, entity, killedEntity, damageSource));
    }
}
