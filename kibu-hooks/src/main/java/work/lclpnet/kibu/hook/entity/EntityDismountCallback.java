package work.lclpnet.kibu.hook.entity;

import net.minecraft.world.entity.Entity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface EntityDismountCallback {

    Hook<EntityDismountCallback> HOOK = HookFactory.createArrayBacked(EntityDismountCallback.class, callbacks -> (entity, vehicle) -> {
        boolean cancel = false;

        for (var cb : callbacks) {
            if (cb.onDismount(entity, vehicle)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onDismount(Entity entity, Entity vehicle);
}
