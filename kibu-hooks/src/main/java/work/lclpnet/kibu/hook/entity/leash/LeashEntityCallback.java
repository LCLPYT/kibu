package work.lclpnet.kibu.hook.entity.leash;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Called when a player leashes an entity by right-clicking it with a lead.
 */
public interface LeashEntityCallback {

    Hook<LeashEntityCallback> HOOK = HookFactory.createArrayBacked(LeashEntityCallback.class, hooks -> (player, entity) -> {
        boolean cancel = false;

        for (var cb : hooks) {
            if (cb.onLeash(player, entity)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onLeash(Player player, Entity entity);
}
