package work.lclpnet.kibu.hook.player;

import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface PlayerTeleportedCallback {

    Hook<PlayerTeleportedCallback> HOOK = HookFactory.createArrayBacked(PlayerTeleportedCallback.class, callbacks -> player -> {
        for (var cb : callbacks) {
            cb.onTeleported(player);
        }
    });

    void onTeleported(ServerPlayerEntity player);
}
