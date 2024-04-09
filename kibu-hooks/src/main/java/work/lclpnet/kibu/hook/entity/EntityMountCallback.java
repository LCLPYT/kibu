package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.Entity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface EntityMountCallback {

    Hook<EntityMountCallback> HOOK = HookFactory.createArrayBacked(EntityMountCallback.class, callbacks -> (entity, vehicle) -> {
        boolean cancel = false;

        for (var cb : callbacks) {
            if (cb.onMount(entity, vehicle)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onMount(Entity entity, Entity vehicle);
}
