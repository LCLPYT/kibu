package work.lclpnet.kibu.map;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class MapUtil {

    public static MapId allocateMapId(ServerLevel world, int x, int z, int scale, boolean showIcons, boolean unlimitedTracking, ResourceKey<Level> dimension) {
        MapItemSavedData mapState = MapItemSavedData.createFresh(x, z, (byte) scale, showIcons, unlimitedTracking, dimension);

        MapId id = world.getFreeMapId();

        world.setMapData(id, mapState);

        return id;
    }

    private MapUtil() {}
}
