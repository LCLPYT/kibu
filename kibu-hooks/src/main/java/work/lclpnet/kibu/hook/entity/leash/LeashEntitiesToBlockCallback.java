package work.lclpnet.kibu.hook.entity.leash;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

import java.util.Collection;

/**
 * Called when a player leashes entities to a block (i.e. fence).
 */
public interface LeashEntitiesToBlockCallback {

    Hook<LeashEntitiesToBlockCallback> HOOK = HookFactory.createArrayBacked(LeashEntitiesToBlockCallback.class, hooks -> (player, pos, entities) -> {
        boolean cancel = false;

        for (var hook : hooks) {
            if (hook.onLeashToBlock(player, pos, entities)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onLeashToBlock(Player player, BlockPos pos, Collection<Entity> entities);
}
