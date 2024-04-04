package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.Entity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface EntityTeleportCallback {

    Hook<EntityTeleportCallback> HOOK = HookFactory.createArrayBacked(EntityTeleportCallback.class, callbacks -> (entity, x, y, z) -> {
        boolean cancel = false;

        for (var callback : callbacks) {
            if (callback.onTeleport(entity, x, y, z)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onTeleport(Entity entity, double x, double y, double z);
}
