package work.lclpnet.kibu.hook.entity.leash;

import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.player.PlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Called when a player takes hold of all entities leashed to a leash knot entity.
 */
public interface LeashKnotTakeCallback {

    Hook<LeashKnotTakeCallback> HOOK = HookFactory.createArrayBacked(LeashKnotTakeCallback.class, hooks -> (player, leashKnot) -> {
        boolean cancel = false;

        for (var cb : hooks) {
            if (cb.onTakeHold(player, leashKnot)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onTakeHold(PlayerEntity player, LeashKnotEntity leashKnot);
}
