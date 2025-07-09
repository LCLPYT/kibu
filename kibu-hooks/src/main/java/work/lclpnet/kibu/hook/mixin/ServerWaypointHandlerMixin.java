package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerWaypointHandler;
import net.minecraft.world.waypoint.ServerWaypoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.player.PlayerWaypointCallback;

@Mixin(ServerWaypointHandler.class)
public class ServerWaypointHandlerMixin {

    @WrapOperation(
            method = {
                    "refreshTracking(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/waypoint/ServerWaypoint;)V",
                    "refreshTracking(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/waypoint/ServerWaypoint;Lnet/minecraft/world/waypoint/ServerWaypoint$WaypointTracker;)V"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerWaypointHandler;isLocatorBarEnabled(Lnet/minecraft/server/network/ServerPlayerEntity;)Z"
            )
    )
    public boolean kibu$isLocatorEnabled(ServerPlayerEntity player, Operation<Boolean> original, @Local(argsOnly = true) ServerWaypoint waypoint) {
        boolean enabled = original.call(player);

        if (!enabled) {
            return false;
        }

        return !PlayerWaypointCallback.HOOK.invoker().onRefreshTracking(player, waypoint);
    }
}
