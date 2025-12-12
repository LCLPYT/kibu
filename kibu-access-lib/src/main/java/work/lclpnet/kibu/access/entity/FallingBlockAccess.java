package work.lclpnet.kibu.access.entity;

import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import work.lclpnet.kibu.access.mixin.FallingBlockEntityAccessor;

public class FallingBlockAccess {

    private FallingBlockAccess() {}

    public static void setBlockState(FallingBlockEntity entity, BlockState state) {
        ((FallingBlockEntityAccessor) entity).setBlockState(state);
    }

    public static void setDropItem(FallingBlockEntity entity, boolean dropItem) {
        entity.dropItem = dropItem;
    }

    public static boolean isDropItem(FallingBlockEntity entity) {
        return entity.dropItem;
    }

    public static void setDestroyedOnLanding(FallingBlockEntity entity, boolean destroyedOnLanding) {
        ((FallingBlockEntityAccessor) entity).setCancelDrop(destroyedOnLanding);
    }

    public static boolean isDestroyedOnLanding(FallingBlockEntity entity) {
        return ((FallingBlockEntityAccessor) entity).getCancelDrop();
    }
}
