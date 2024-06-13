package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.Leashable;
import net.minecraft.entity.player.PlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface UnleashEntityCallback {

    Hook<UnleashEntityCallback> HOOK = HookFactory.createArrayBacked(UnleashEntityCallback.class,
            callbacks -> (player, entity) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onUnleash(player, entity)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    boolean onUnleash(PlayerEntity player, Leashable entity);
}
