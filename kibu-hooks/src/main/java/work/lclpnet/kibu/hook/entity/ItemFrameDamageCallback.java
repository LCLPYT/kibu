package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ItemFrameEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface ItemFrameDamageCallback {

    Hook<ItemFrameDamageCallback> HOOK = HookFactory.createArrayBacked(ItemFrameDamageCallback.class,
            callbacks -> (itemFrame, source, amount) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onDamage(itemFrame, source, amount)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    boolean onDamage(ItemFrameEntity itemFrame, DamageSource source, float amount);
}
