package work.lclpnet.kibu.hook.player;

import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;
import work.lclpnet.kibu.hook.util.PositionRotation;

public interface PlayerMoveCallback {

    Hook<PlayerMoveCallback> HOOK = HookFactory.createArrayBacked(PlayerMoveCallback.class, hooks -> (player, from, to) -> {
        boolean cancelled = false;

        for (var hook : hooks) {
            if (hook.onMove(player, from, to)) {
                cancelled = true;
            }
        }

        return cancelled;
    });

    boolean onMove(ServerPlayerEntity player, PositionRotation from, PositionRotation to);
}
