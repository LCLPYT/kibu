package work.lclpnet.kibu.hook.entity.leash;

import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
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

    boolean onTakeHold(Player player, LeashFenceKnotEntity leashKnot);
}
