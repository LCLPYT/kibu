package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.BoatEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface BoatDamageCallback {

    Hook<BoatDamageCallback> HOOK = HookFactory.createArrayBacked(BoatDamageCallback.class,
            callbacks -> (boat, source, amount) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onDamage(boat, source, amount)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    boolean onDamage(BoatEntity boat, DamageSource source, float amount);
}
