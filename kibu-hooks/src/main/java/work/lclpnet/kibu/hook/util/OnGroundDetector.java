package work.lclpnet.kibu.hook.util;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;

public class OnGroundDetector {

    public static boolean isOnGroundServer(ServerPlayerEntity player) {
        return isOnGroundServer(player, 0.02);
    }

    public static boolean isOnGroundServer(ServerPlayerEntity player, double tol) {
        double y = player.getY();
        Box box = player.getBoundingBox().withMinY(y - tol).withMaxY(y + 1e-5);

        return player.getEntityWorld().getBlockCollisions(player, box).iterator().hasNext();
    }
}
