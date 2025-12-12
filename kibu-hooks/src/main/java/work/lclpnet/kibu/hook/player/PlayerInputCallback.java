package work.lclpnet.kibu.hook.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Input;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface PlayerInputCallback {

    /** Invoked when a player's input changes. The previous input is available with <code>player.getPlayerInput()</code> */
    Hook<PlayerInputCallback> HOOK = HookFactory.createArrayBacked(PlayerInputCallback.class, callbacks -> (player, input) -> {
        for (var cb : callbacks) {
            cb.onInput(player, input);
        }
    });

    void onInput(ServerPlayer player, Input input);
}
