package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface MinecartDamageCallback {

    Hook<MinecartDamageCallback> HOOK = HookFactory.createArrayBacked(MinecartDamageCallback.class,
            callbacks -> (minecart, source, amount) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onDamage(minecart, source, amount)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    boolean onDamage(AbstractMinecartEntity minecart, DamageSource source, float amount);
}
