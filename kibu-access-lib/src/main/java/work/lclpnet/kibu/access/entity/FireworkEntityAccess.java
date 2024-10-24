package work.lclpnet.kibu.access.entity;

import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import work.lclpnet.kibu.access.mixin.FireworkRocketEntityAccessor;

public class FireworkEntityAccess {

    private FireworkEntityAccess() {}

    public static void explode(FireworkRocketEntity fireworkRocket) {
        World world = fireworkRocket.getWorld();

        if (world instanceof ServerWorld serverWorld) {
            ((FireworkRocketEntityAccessor) fireworkRocket).invokeExplodeAndRemove(serverWorld);
        }
    }
}
