package work.lclpnet.kibu.access.entity;

import net.minecraft.world.entity.decoration.ArmorStand;
import work.lclpnet.kibu.access.mixin.ArmorStandEntityAccessor;

public class ArmorStandAccess {

    private ArmorStandAccess() {}

    public static void setSmall(ArmorStand entity, boolean small) {
        ((ArmorStandEntityAccessor) entity).invokeSetSmall(small);
    }

    public static void setMarker(ArmorStand entity, boolean marker) {
        ((ArmorStandEntityAccessor) entity).invokeSetMarker(marker);
    }
}
