package work.lclpnet.kibu.hook.player;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Called when the player swings an arm.
 */
public interface PlayerSwingHandHook {

    Hook<PlayerSwingHandHook> HOOK = HookFactory.createArrayBacked(PlayerSwingHandHook.class, callbacks -> (player, hand) -> {
        for (var cb : callbacks) {
            cb.onSwingHand(player, hand);
        }
    });

    void onSwingHand(ServerPlayerEntity player, Hand hand);
}
