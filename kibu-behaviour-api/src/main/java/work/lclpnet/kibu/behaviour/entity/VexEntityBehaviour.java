package work.lclpnet.kibu.behaviour.entity;

import net.minecraft.world.entity.monster.Vex;
import work.lclpnet.kibu.behaviour.type.KibuVexEntity;

public class VexEntityBehaviour {

    private VexEntityBehaviour() {}

    public static void setForceClipping(Vex vex, boolean clipping) {
        ((KibuVexEntity) vex).kibu$setForceClipping(clipping);
    }

    public static boolean isForceClipping(Vex vex) {
        return ((KibuVexEntity) vex).kibu$isForceClipping();
    }
}
