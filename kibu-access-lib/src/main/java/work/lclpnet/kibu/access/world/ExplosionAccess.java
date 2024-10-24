package work.lclpnet.kibu.access.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.ExplosionImpl;
import work.lclpnet.kibu.access.mixin.ExplosionImplAccessor;

import java.util.List;

public class ExplosionAccess {

    private ExplosionAccess() {}

    public static List<BlockPos> getBlocksToDestroy(ExplosionImpl explosion) {
        return ((ExplosionImplAccessor) explosion).invokeGetBlocksToDestroy();
    }

    public static void damageEntities(ExplosionImpl explosion) {
        ((ExplosionImplAccessor) explosion).invokeDamageEntities();
    }

    public static void destroyBlocks(ExplosionImpl explosion, List<BlockPos> positions) {
        ((ExplosionImplAccessor) explosion).invokeDestroyBlocks(positions);
    }

    public static void createFire(ExplosionImpl explosion, List<BlockPos> positions) {
        ((ExplosionImplAccessor) explosion).invokeCreateFire(positions);
    }
}
