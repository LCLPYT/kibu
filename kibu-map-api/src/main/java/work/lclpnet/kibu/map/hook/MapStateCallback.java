package work.lclpnet.kibu.map.hook;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface MapStateCallback {

    Hook<MapStateCallback> HOOK = HookFactory.createArrayBacked(MapStateCallback.class,
            callbacks -> (world, id) -> {
                for (var cb : callbacks) {
                    MapItemSavedData mapState = cb.getMapState(world, id);

                    if (mapState != null) {
                        return mapState;
                    }
                }

                return null;
            });

    @Nullable
    MapItemSavedData getMapState(ServerLevel world, MapId id);
}
