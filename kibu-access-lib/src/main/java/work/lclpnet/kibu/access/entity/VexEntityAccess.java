package work.lclpnet.kibu.access.entity;

import net.minecraft.entity.mob.VexEntity;
import org.jetbrains.annotations.ApiStatus;
import work.lclpnet.kibu.access.type.KibuVexEntity;

public class VexEntityAccess {

    private VexEntityAccess() {}

    @ApiStatus.Experimental
    public static void setForceClipping(VexEntity vex, boolean clipping) {
        ((KibuVexEntity) vex).kibu$setForceClipping(clipping);
    }

    @ApiStatus.Experimental
    public static boolean isForceClipping(VexEntity vex) {
        return ((KibuVexEntity) vex).kibu$isForceClipping();
    }
}
