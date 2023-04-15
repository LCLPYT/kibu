package work.lclpnet.kibu.hook.player;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface PlayerGameModeChangeCallback {

    Hook<PlayerGameModeChangeCallback> HOOK = HookFactory.createArrayBacked(PlayerGameModeChangeCallback.class, (hooks) -> (player, gameMode) -> {
        for (var hook : hooks) {
            hook.onChangeGameMode(player, gameMode);
        }
    });

    void onChangeGameMode(ServerPlayerEntity player, GameMode gameMode);
}
