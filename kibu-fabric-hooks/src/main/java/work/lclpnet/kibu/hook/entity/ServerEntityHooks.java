package work.lclpnet.kibu.hook.entity;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import work.lclpnet.mplugins.hook.Hook;
import work.lclpnet.mplugins.hook.HookFactory;

/**
 * Hook versions of {@link ServerEntityEvents} from fabric-lifecycle-events.
 */
public class ServerEntityHooks {

    public static final Hook<ServerEntityEvents.Load> ENTITY_LOAD = HookFactory.createArrayBacked(ServerEntityEvents.Load.class, callbacks -> (entity, world) -> {
        for (ServerEntityEvents.Load callback : callbacks) {
            callback.onLoad(entity, world);
        }
    });

    public static final Hook<ServerEntityEvents.Unload> ENTITY_UNLOAD = HookFactory.createArrayBacked(ServerEntityEvents.Unload.class, callbacks -> (entity, world) -> {
        for (ServerEntityEvents.Unload callback : callbacks) {
            callback.onUnload(entity, world);
        }
    });

    public static final Hook<ServerEntityEvents.EquipmentChange> EQUIPMENT_CHANGE = HookFactory.createArrayBacked(ServerEntityEvents.EquipmentChange.class, callbacks -> (livingEntity, equipmentSlot, previous, next) -> {
        for (ServerEntityEvents.EquipmentChange callback : callbacks) {
            callback.onChange(livingEntity, equipmentSlot, previous, next);
        }
    });

    static {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> ENTITY_LOAD.invoker().onLoad(entity, world));
        ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> ENTITY_UNLOAD.invoker().onUnload(entity, world));
        ServerEntityEvents.EQUIPMENT_CHANGE.register((livingEntity, equipmentSlot, previousStack, currentStack) -> EQUIPMENT_CHANGE.invoker().onChange(livingEntity, equipmentSlot, previousStack, currentStack));
    }
}
