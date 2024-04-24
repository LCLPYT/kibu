package work.lclpnet.kibu.behaviour.entity;

import net.minecraft.entity.mob.VexEntity;
import work.lclpnet.kibu.behaviour.type.KibuVexEntity;

public class VexEntityBehaviour {

    private VexEntityBehaviour() {}

    public static void setForceClipping(VexEntity vex, boolean clipping) {
        ((KibuVexEntity) vex).kibu$setForceClipping(clipping);
    }

    public static boolean isForceClipping(VexEntity vex) {
        return ((KibuVexEntity) vex).kibu$isForceClipping();
    }
}
