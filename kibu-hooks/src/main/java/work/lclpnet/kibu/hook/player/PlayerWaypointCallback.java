package work.lclpnet.kibu.hook.player;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.waypoint.ServerWaypoint;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface PlayerWaypointCallback {

    Hook<PlayerWaypointCallback> HOOK = HookFactory.createArrayBacked(PlayerWaypointCallback.class, hooks -> (player, waypoint) -> {
        boolean cancel = false;

        for (var hook : hooks) {
            if (hook.onRefreshTracking(player, waypoint)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onRefreshTracking(ServerPlayerEntity player, ServerWaypoint waypoint);
}
