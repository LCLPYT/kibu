package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.LivingEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface EntityHealthCallback {

    Hook<EntityHealthCallback> HOOK = HookFactory.createArrayBacked(EntityHealthCallback.class, callbacks
            -> (entity, health) -> {

        boolean cancel = false;

        for (var callback : callbacks) {
            if (callback.onHealthChange(entity, health)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onHealthChange(LivingEntity entity, float health);
}
