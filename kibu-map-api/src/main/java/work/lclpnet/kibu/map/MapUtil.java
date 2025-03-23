package work.lclpnet.kibu.map;

import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.map.MapState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class MapUtil {

    public static MapIdComponent allocateMapId(ServerWorld world, int x, int z, int scale, boolean showIcons, boolean unlimitedTracking, RegistryKey<World> dimension) {
        MapState mapState = MapState.of(x, z, (byte) scale, showIcons, unlimitedTracking, dimension);

        MapIdComponent id = world.increaseAndGetMapId();

        world.putMapState(id, mapState);

        return id;
    }

    private MapUtil() {}
}
