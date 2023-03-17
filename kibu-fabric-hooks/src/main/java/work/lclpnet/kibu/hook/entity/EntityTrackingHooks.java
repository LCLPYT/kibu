package work.lclpnet.kibu.hook.entity;

import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Hook versions of {@link EntityTrackingEvents} from fabric-networking-api.
 */
public class EntityTrackingHooks {

    public static final Hook<EntityTrackingEvents.StartTracking> START_TRACKING = HookFactory.createArrayBacked(EntityTrackingEvents.StartTracking.class,
            callbacks -> (trackedEntity, player) -> {
                for (EntityTrackingEvents.StartTracking callback : callbacks) {
                    callback.onStartTracking(trackedEntity, player);
                }
            });

    public static final Hook<EntityTrackingEvents.StopTracking> STOP_TRACKING = HookFactory.createArrayBacked(EntityTrackingEvents.StopTracking.class,
            callbacks -> (trackedEntity, player) -> {
                for (EntityTrackingEvents.StopTracking callback : callbacks) {
                    callback.onStopTracking(trackedEntity, player);
                }
            });

    static {
        EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) -> START_TRACKING.invoker().onStartTracking(trackedEntity, player));
        EntityTrackingEvents.STOP_TRACKING.register((trackedEntity, player) -> STOP_TRACKING.invoker().onStopTracking(trackedEntity, player));
    }
}
