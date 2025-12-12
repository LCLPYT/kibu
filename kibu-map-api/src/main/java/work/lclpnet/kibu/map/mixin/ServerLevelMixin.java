package work.lclpnet.kibu.map.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.map.hook.MapStateCallback;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    @Inject(
            method = "getMapData",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onGetMapState(MapId id, CallbackInfoReturnable<MapItemSavedData> cir) {
        ServerLevel world = (ServerLevel) (Object) this;

        MapItemSavedData override = MapStateCallback.HOOK.invoker().getMapState(world, id);

        if (override != null) {
            cir.setReturnValue(override);
        }
    }
}
