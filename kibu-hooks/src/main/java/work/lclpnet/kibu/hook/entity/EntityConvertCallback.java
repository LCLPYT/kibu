package work.lclpnet.kibu.hook.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface EntityConvertCallback {

    Hook<EntityConvertCallback> HOOK = HookFactory.createArrayBacked(EntityConvertCallback.class, callbacks -> (entity, type) -> {
        boolean cancelled = false;

        for (var callback : callbacks) {
            if (callback.onConvert(entity, type)) {
                cancelled = true;
            }
        }

        return cancelled;
    });

    boolean onConvert(Mob entity, EntityType<?> type);
}
