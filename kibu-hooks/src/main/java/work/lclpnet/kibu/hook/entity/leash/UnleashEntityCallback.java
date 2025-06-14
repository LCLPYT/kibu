package work.lclpnet.kibu.hook.entity.leash;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Called when a player detaches a leash from an entity by right-clicking an entity leashed to them.
 */
public interface UnleashEntityCallback {

    Hook<UnleashEntityCallback> HOOK = HookFactory.createArrayBacked(UnleashEntityCallback.class, hooks -> (player, entity) -> {
        boolean cancel = false;

        for (var hook : hooks) {
            if (hook.onUnleash(player, entity)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onUnleash(PlayerEntity player, Entity entity);
}
