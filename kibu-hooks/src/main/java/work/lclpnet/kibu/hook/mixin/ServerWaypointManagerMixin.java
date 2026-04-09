package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.waypoints.ServerWaypointManager;
import net.minecraft.world.waypoints.WaypointTransmitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.player.PlayerWaypointCallback;

@Mixin(ServerWaypointManager.class)
public class ServerWaypointManagerMixin {

    @WrapOperation(
            method = {
                    "createConnection(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/waypoints/WaypointTransmitter;)V",
                    "updateConnection(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/waypoints/WaypointTransmitter;Lnet/minecraft/world/waypoints/WaypointTransmitter$Connection;)V"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/waypoints/ServerWaypointManager;isLocatorBarEnabledFor(Lnet/minecraft/server/level/ServerPlayer;)Z"
            )
    )
    public boolean kibu$isLocatorEnabled(ServerPlayer player, Operation<Boolean> original, @Local(argsOnly = true, name = "waypoint") WaypointTransmitter waypoint) {
        boolean enabled = original.call(player);

        if (!enabled) {
            return false;
        }

        return !PlayerWaypointCallback.HOOK.invoker().onRefreshTracking(player, waypoint);
    }
}
