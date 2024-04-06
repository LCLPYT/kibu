package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Called when an entity applies an effect to another entity.
 */
public interface EntityStatusEffectCallback {

    Hook<EntityStatusEffectCallback> HOOK = HookFactory.createArrayBacked(EntityStatusEffectCallback.class, callbacks -> (entity, effect, source) -> {
        boolean cancel = false;

        for (var cb : callbacks) {
            if (cb.onAddEffect(entity, effect, source)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onAddEffect(LivingEntity entity, StatusEffectInstance effect, @Nullable Entity source);
}
