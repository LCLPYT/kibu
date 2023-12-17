package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.player.PlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface LeashDetachCallback {

    Hook<LeashDetachCallback> HOOK = HookFactory.createArrayBacked(LeashDetachCallback.class,
            callbacks -> (player, leashKnot) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onDetach(player, leashKnot)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    boolean onDetach(PlayerEntity player, LeashKnotEntity leashKnot);
}
