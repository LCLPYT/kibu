package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface DecorationEntityDamageCallback {

    Hook<DecorationEntityDamageCallback> HOOK = HookFactory.createArrayBacked(DecorationEntityDamageCallback.class,
            callbacks -> (entity, source, amount) -> {
                boolean cancel = false;

                for (var callback : callbacks) {
                    if (callback.onDamage(entity, source, amount)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    boolean onDamage(AbstractDecorationEntity entity, DamageSource source, float amount);
}
