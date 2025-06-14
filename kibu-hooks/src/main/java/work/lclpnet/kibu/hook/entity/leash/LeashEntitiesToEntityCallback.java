package work.lclpnet.kibu.hook.entity.leash;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

import java.util.Collection;

/**
 * Called when a player leashes entities to another entity.
 */
public interface LeashEntitiesToEntityCallback {

    Hook<LeashEntitiesToEntityCallback> HOOK = HookFactory.createArrayBacked(LeashEntitiesToEntityCallback.class, hooks -> (player, leashHolder, entities) -> {
        boolean cancel = false;

        for (var hook : hooks) {
            if (hook.onLeashToEntity(player, leashHolder, entities)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onLeashToEntity(PlayerEntity player, Entity leashHolder, Collection<Entity> entities);
}
