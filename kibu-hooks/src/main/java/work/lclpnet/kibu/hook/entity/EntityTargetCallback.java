package work.lclpnet.kibu.hook.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface EntityTargetCallback {

    Hook<EntityTargetCallback> HOOK = HookFactory.createArrayBacked(EntityTargetCallback.class, callbacks -> (entity, target) -> {
        boolean cancel = false;

        for (var callback : callbacks) {
            if (callback.onChangeTarget(entity, target)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onChangeTarget(Mob entity, @Nullable LivingEntity target);
}
