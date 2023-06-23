package work.lclpnet.kibu.access.entity;

import net.minecraft.entity.decoration.ArmorStandEntity;
import work.lclpnet.kibu.access.mixin.ArmorStandEntityAccessor;

public class ArmorStandAccess {

    private ArmorStandAccess() {}

    public static void setSmall(ArmorStandEntity entity, boolean small) {
        ((ArmorStandEntityAccessor) entity).invokeSetSmall(small);
    }

    public static void setMarker(ArmorStandEntity entity, boolean marker) {
        ((ArmorStandEntityAccessor) entity).invokeSetMarker(marker);
    }
}
