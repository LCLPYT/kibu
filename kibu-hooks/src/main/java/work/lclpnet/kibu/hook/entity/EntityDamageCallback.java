package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Hook that is fired before an entity takes damage.
 * This is fired after the ALLOW_DAMAGE hook and vanilla damage checks.
 * Cancelling an invocation will result in not modifying the player's health, as well as their armor durability etc.
 * Shield durability is not affected by cancelling the hook though.
 */
public interface EntityDamageCallback {

    Hook<EntityDamageCallback> HOOK = HookFactory.createArrayBacked(EntityDamageCallback.class, callbacks -> (entity, source, amount) -> {
        boolean cancel = false;

        for (var cb : callbacks) {
            if (cb.onDamage(entity, source, amount)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onDamage(LivingEntity entity, DamageSource source, float amount);
}
