package work.lclpnet.kibu.hook.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;

public class OnGroundDetector {

    public static boolean isOnGroundServer(ServerPlayer player) {
        return isOnGroundServer(player, 0.02);
    }

    public static boolean isOnGroundServer(ServerPlayer player, double tol) {
        double y = player.getY();
        AABB box = player.getBoundingBox().setMinY(y - tol).setMaxY(y + 1e-5);

        return player.level().getBlockCollisions(player, box).iterator().hasNext();
    }
}
