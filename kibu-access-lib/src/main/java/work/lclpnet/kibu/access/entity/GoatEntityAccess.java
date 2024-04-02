package work.lclpnet.kibu.access.entity;

import net.minecraft.entity.passive.GoatEntity;
import work.lclpnet.kibu.access.type.KibuGoatEntity;

public class GoatEntityAccess {

    private GoatEntityAccess() {}

    public static void setLeftHorn(GoatEntity goat, boolean exists) {
        ((KibuGoatEntity) goat).kibu$setLeftHorn(exists);
    }

    public static void setRightHorn(GoatEntity goat, boolean exists) {
        ((KibuGoatEntity) goat).kibu$setRightHorn(exists);
    }
}
