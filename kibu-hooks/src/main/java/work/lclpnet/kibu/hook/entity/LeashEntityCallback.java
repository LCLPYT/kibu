package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface LeashEntityCallback {

    Hook<LeashEntityCallback> HOOK = HookFactory.createArrayBacked(LeashEntityCallback.class,
            callbacks -> (player, entity) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onLeash(player, entity)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    boolean onLeash(PlayerEntity player, MobEntity entity);
}
