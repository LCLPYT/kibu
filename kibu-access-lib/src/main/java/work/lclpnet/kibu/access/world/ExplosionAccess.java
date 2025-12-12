package work.lclpnet.kibu.access.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerExplosion;
import work.lclpnet.kibu.access.mixin.ExplosionImplAccessor;

import java.util.List;

public class ExplosionAccess {

    private ExplosionAccess() {}

    public static List<BlockPos> getBlocksToDestroy(ServerExplosion explosion) {
        return ((ExplosionImplAccessor) explosion).invokeCalculateExplodedPositions();
    }

    public static void damageEntities(ServerExplosion explosion) {
        ((ExplosionImplAccessor) explosion).invokeHurtEntities();
    }

    public static void destroyBlocks(ServerExplosion explosion, List<BlockPos> positions) {
        ((ExplosionImplAccessor) explosion).invokeInteractWithBlocks(positions);
    }

    public static void createFire(ServerExplosion explosion, List<BlockPos> positions) {
        ((ExplosionImplAccessor) explosion).invokeCreateFire(positions);
    }
}
