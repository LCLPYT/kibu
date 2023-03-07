package work.lclpnet.kibu.hook.entity;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import work.lclpnet.mplugins.hook.Hook;
import work.lclpnet.mplugins.hook.HookFactory;

/**
 * Hook versions of {@link ServerLivingEntityEvents} from fabric-entity-events.
 */
public class ServerLivingEntityHooks {

    public static final Hook<ServerLivingEntityEvents.AllowDamage> ALLOW_DAMAGE = HookFactory.createArrayBacked(ServerLivingEntityEvents.AllowDamage.class,
            callbacks -> (entity, source, amount) -> {
                for (ServerLivingEntityEvents.AllowDamage callback : callbacks) {
                    if (!callback.allowDamage(entity, source, amount)) {
                        return false;
                    }
                }

                return true;
            });

    public static final Hook<ServerLivingEntityEvents.AllowDeath> ALLOW_DEATH = HookFactory.createArrayBacked(ServerLivingEntityEvents.AllowDeath.class,
            callbacks -> (entity, damageSource, damageAmount) -> {
                for (ServerLivingEntityEvents.AllowDeath callback : callbacks) {
                    if (!callback.allowDeath(entity, damageSource, damageAmount)) {
                        return false;
                    }
                }

                return true;
            });

    public static final Event<ServerLivingEntityEvents.AfterDeath> AFTER_DEATH = EventFactory.createArrayBacked(ServerLivingEntityEvents.AfterDeath.class,
            callbacks -> (entity, damageSource) -> {
                for (ServerLivingEntityEvents.AfterDeath callback : callbacks) {
                    callback.afterDeath(entity, damageSource);
                }
            });

    static {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> ALLOW_DAMAGE.invoker().allowDamage(entity, source, amount));
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> ALLOW_DEATH.invoker().allowDeath(entity, damageSource, damageAmount));
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> AFTER_DEATH.invoker().afterDeath(entity, damageSource));
    }
}
