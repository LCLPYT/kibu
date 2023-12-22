package work.lclpnet.kibu.access.entity;

import net.minecraft.entity.projectile.FireworkRocketEntity;
import work.lclpnet.kibu.access.mixin.FireworkRocketEntityAccessor;

public class FireworkEntityAccess {

    private FireworkEntityAccess() {}

    public static void explode(FireworkRocketEntity fireworkRocket) {
        ((FireworkRocketEntityAccessor) fireworkRocket).invokeExplodeAndRemove();
    }
}
