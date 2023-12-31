package work.lclpnet.kibu.access.entity;

import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.util.math.Direction;
import work.lclpnet.kibu.access.mixin.AbstractDecorationEntityAccessor;

public class DecorationEntityAccess {

    private DecorationEntityAccess() {}

    public static void setFacing(AbstractDecorationEntity deco, Direction facing) {
        ((AbstractDecorationEntityAccessor) deco).invokeSetFacing(facing);
    }
}
