package work.lclpnet.kibu.access.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.FallingBlockEntity;
import work.lclpnet.kibu.access.mixin.FallingBlockEntityAccessor;

public class FallingBlockAccess {

    private FallingBlockAccess() {}

    public static void setBlockState(FallingBlockEntity entity, BlockState state) {
        ((FallingBlockEntityAccessor) entity).setBlock(state);
    }

    public static void setDropItem(FallingBlockEntity entity, boolean dropItem) {
        entity.dropItem = dropItem;
    }

    public static boolean isDropItem(FallingBlockEntity entity) {
        return entity.dropItem;
    }

    public static void setDestroyedOnLanding(FallingBlockEntity entity, boolean destroyedOnLanding) {
        ((FallingBlockEntityAccessor) entity).setDestroyedOnLanding(destroyedOnLanding);
    }

    public static boolean isDestroyedOnLanding(FallingBlockEntity entity) {
        return ((FallingBlockEntityAccessor) entity).getDestroyedOnLanding();
    }
}
