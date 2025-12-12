package work.lclpnet.kibu.hook.player;

import net.minecraft.server.level.ServerPlayer;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface PlayerSneakCallback {

    Hook<PlayerSneakCallback> HOOK = HookFactory.createArrayBacked(PlayerSneakCallback.class, callbacks -> (player, sneaking) -> {
        boolean cancelled = false;

        for (var callback : callbacks) {
            if (callback.onSneak(player, sneaking)) {
                cancelled = true;
            }
        }

        return cancelled;
    });

    boolean onSneak(ServerPlayer player, boolean sneaking);
}
