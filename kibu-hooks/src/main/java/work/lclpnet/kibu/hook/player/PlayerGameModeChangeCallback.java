package work.lclpnet.kibu.hook.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface PlayerGameModeChangeCallback {

    Hook<PlayerGameModeChangeCallback> HOOK = HookFactory.createArrayBacked(PlayerGameModeChangeCallback.class, (hooks) -> (player, gameMode) -> {
        for (var hook : hooks) {
            hook.onChangeGameMode(player, gameMode);
        }
    });

    void onChangeGameMode(ServerPlayer player, GameType gameMode);
}
