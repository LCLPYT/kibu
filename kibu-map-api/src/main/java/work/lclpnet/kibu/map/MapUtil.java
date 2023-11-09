package work.lclpnet.kibu.map;

import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public class MapUtil {

    public static void setMapId(ItemStack stack, int id) {
        stack.getOrCreateNbt().putInt("map", id);
    }

    public static int allocateMapId(World world, int x, int z, int scale, boolean showIcons, boolean unlimitedTracking, RegistryKey<World> dimension) {
        MapState mapState = MapState.of(x, z, (byte) scale, showIcons, unlimitedTracking, dimension);

        int id = world.getNextMapId();
        String mapName = FilledMapItem.getMapName(id);

        world.putMapState(mapName, mapState);

        return id;
    }

    private MapUtil() {}
}
