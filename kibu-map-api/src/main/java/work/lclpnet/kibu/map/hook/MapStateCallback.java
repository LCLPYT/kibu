package work.lclpnet.kibu.map.hook;

import net.minecraft.item.map.MapState;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface MapStateCallback {

    Hook<MapStateCallback> HOOK = HookFactory.createArrayBacked(MapStateCallback.class,
            callbacks -> (world, id) -> {
                for (var cb : callbacks) {
                    MapState mapState = cb.getMapState(world, id);

                    if (mapState != null) {
                        return mapState;
                    }
                }

                return null;
            });

    @Nullable
    MapState getMapState(ServerWorld world, String id);
}
