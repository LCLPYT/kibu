package work.lclpnet.kibu.map.mixin;

import net.minecraft.item.map.MapState;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.map.hook.MapStateCallback;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @Inject(
            method = "getMapState",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onGetMapState(String id, CallbackInfoReturnable<MapState> cir) {
        ServerWorld world = (ServerWorld) (Object) this;

        MapState override = MapStateCallback.HOOK.invoker().getMapState(world, id);

        if (override != null) {
            cir.setReturnValue(override);
        }
    }
}
