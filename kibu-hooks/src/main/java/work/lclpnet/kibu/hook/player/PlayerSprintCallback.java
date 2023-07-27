package work.lclpnet.kibu.hook.player;

import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface PlayerSprintCallback {

    Hook<PlayerSprintCallback> HOOK = HookFactory.createArrayBacked(PlayerSprintCallback.class, callbacks -> (player, sprinting) -> {
        for (var callback : callbacks) {
            callback.onSprint(player, sprinting);
        }
    });

    void onSprint(ServerPlayerEntity player, boolean sprinting);
}
