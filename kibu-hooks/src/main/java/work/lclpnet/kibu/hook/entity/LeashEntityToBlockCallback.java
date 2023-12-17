package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface LeashEntityToBlockCallback {

    Hook<LeashEntityToBlockCallback> HOOK = HookFactory.createArrayBacked(LeashEntityToBlockCallback.class,
            callbacks -> (player, entity, leashKnot) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onLeashToBlock(player, entity, leashKnot)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    boolean onLeashToBlock(PlayerEntity player, MobEntity entity, LeashKnotEntity leashKnot);
}
