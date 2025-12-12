package work.lclpnet.kibu.access.entity;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.decoration.HangingEntity;
import work.lclpnet.kibu.access.mixin.AbstractDecorationEntityAccessor;

public class DecorationEntityAccess {

    private DecorationEntityAccess() {}

    public static void setFacing(HangingEntity deco, Direction facing) {
        ((AbstractDecorationEntityAccessor) deco).invokeSetDirection(facing);
    }
}
