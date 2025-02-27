package work.lclpnet.kibu.access.entity;

import net.minecraft.entity.MarkerEntity;
import net.minecraft.nbt.NbtCompound;
import work.lclpnet.kibu.access.mixin.MarkerEntityAccessor;

public class MarkerEntityAccess {

    private MarkerEntityAccess() {}

    public static NbtCompound getData(MarkerEntity marker) {
        return ((MarkerEntityAccessor) marker).getData();
    }
}
