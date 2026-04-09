package work.lclpnet.kibu.access.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerExplosion;
import work.lclpnet.kibu.access.mixin.ServerExplosionAccessor;

import java.util.List;

public class ExplosionAccess {

    private ExplosionAccess() {}

    public static List<BlockPos> getBlocksToDestroy(ServerExplosion explosion) {
        return ((ServerExplosionAccessor) explosion).invokeCalculateExplodedPositions();
    }

    public static void damageEntities(ServerExplosion explosion) {
        ((ServerExplosionAccessor) explosion).invokeHurtEntities();
    }

    public static void destroyBlocks(ServerExplosion explosion, List<BlockPos> positions) {
        ((ServerExplosionAccessor) explosion).invokeInteractWithBlocks(positions);
    }

    public static void createFire(ServerExplosion explosion, List<BlockPos> positions) {
        ((ServerExplosionAccessor) explosion).invokeCreateFire(positions);
    }
}
