package work.lclpnet.kibu.access.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.level.Level;
import work.lclpnet.kibu.access.mixin.FireworkRocketEntityAccessor;

public class FireworkEntityAccess {

    private FireworkEntityAccess() {}

    public static void explode(FireworkRocketEntity fireworkRocket) {
        Level world = fireworkRocket.level();

        if (world instanceof ServerLevel serverWorld) {
            ((FireworkRocketEntityAccessor) fireworkRocket).invokeExplode(serverWorld);
        }
    }
}
