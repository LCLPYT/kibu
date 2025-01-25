package work.lclpnet.kibu.hook.player;

import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface PlayerJumpCallback {

    Hook<PlayerJumpCallback> HOOK = HookFactory.createArrayBacked(PlayerJumpCallback.class, callbacks -> (player) -> {
        boolean cancel = false;

        for (var cb : callbacks) {
            if (cb.onJump(player)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onJump(ServerPlayerEntity player);
}
