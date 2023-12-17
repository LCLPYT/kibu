package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface NonLivingDamageCallback {

    Hook<NonLivingDamageCallback> HOOK = HookFactory.createArrayBacked(NonLivingDamageCallback.class,
            callbacks -> (entity, source, amount) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onDamage(entity, source, amount)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    boolean onDamage(Entity entity, DamageSource source, float amount);
}
