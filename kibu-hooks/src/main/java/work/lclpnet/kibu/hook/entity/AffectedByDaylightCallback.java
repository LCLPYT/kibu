package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.mob.MobEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface AffectedByDaylightCallback {

    Hook<AffectedByDaylightCallback> HOOK = HookFactory.createArrayBacked(AffectedByDaylightCallback.class, callbacks -> entity -> {
        boolean ignore = false;

        for (var callback : callbacks) {
            if (callback.shouldIgnoreDaylight(entity)) {
                ignore = true;
            }
        }

        return ignore;
    });

    boolean shouldIgnoreDaylight(MobEntity entity);
}
