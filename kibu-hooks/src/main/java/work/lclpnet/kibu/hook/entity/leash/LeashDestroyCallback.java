package work.lclpnet.kibu.hook.entity.leash;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Called when a player destroys all leashes of a leash holder entity by right-clicking it with shears.
 * The leashed entity might be a leash knot entity (a leash holder) or an entity that is leashed with another entity.
 */
public interface LeashDestroyCallback {

    Hook<LeashDestroyCallback> HOOK = HookFactory.createArrayBacked(LeashDestroyCallback.class, hooks -> (player, leashed) -> {
        boolean cancel = false;

        for (var hook : hooks) {
            if (hook.onLeashDestroy(player, leashed)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onLeashDestroy(Player player, Entity leashed);
}
