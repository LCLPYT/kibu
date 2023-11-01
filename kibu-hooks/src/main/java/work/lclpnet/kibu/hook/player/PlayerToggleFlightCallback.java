package work.lclpnet.kibu.hook.player;

import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface PlayerToggleFlightCallback {

    Hook<PlayerToggleFlightCallback> HOOK = HookFactory.createArrayBacked(PlayerToggleFlightCallback.class,
            callbacks -> (player, fly) -> {

        boolean cancel = false;

        for (var callback : callbacks) {
            if (callback.onToggleFlight(player, fly)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onToggleFlight(ServerPlayerEntity player, boolean fly);
}
