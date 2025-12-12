package work.lclpnet.kibu.access.entity;

import net.minecraft.world.entity.decoration.ArmorStand;
import work.lclpnet.kibu.access.mixin.ArmorStandAccessor;

public class ArmorStandAccess {

    private ArmorStandAccess() {}

    public static void setSmall(ArmorStand entity, boolean small) {
        ((ArmorStandAccessor) entity).invokeSetSmall(small);
    }

    public static void setMarker(ArmorStand entity, boolean marker) {
        ((ArmorStandAccessor) entity).invokeSetMarker(marker);
    }
}
