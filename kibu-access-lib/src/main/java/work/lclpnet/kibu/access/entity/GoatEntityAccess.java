package work.lclpnet.kibu.access.entity;

import net.minecraft.world.entity.animal.goat.Goat;
import work.lclpnet.kibu.access.type.KibuGoatEntity;

public class GoatEntityAccess {

    private GoatEntityAccess() {}

    public static void setLeftHorn(Goat goat, boolean exists) {
        ((KibuGoatEntity) goat).kibu$setLeftHorn(exists);
    }

    public static void setRightHorn(Goat goat, boolean exists) {
        ((KibuGoatEntity) goat).kibu$setRightHorn(exists);
    }
}
