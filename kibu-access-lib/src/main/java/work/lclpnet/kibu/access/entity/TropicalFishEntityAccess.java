package work.lclpnet.kibu.access.entity;

import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.item.DyeColor;
import work.lclpnet.kibu.access.mixin.TropicalFishEntityAccessor;

public class TropicalFishEntityAccess {

    private TropicalFishEntityAccess() {}

    public static void setVariant(TropicalFish tropicalFish, TropicalFish.Pattern pattern, DyeColor baseColor, DyeColor patternColor) {
        int id = TropicalFishEntityAccessor.invokePackVariant(pattern, baseColor, patternColor);
        ((TropicalFishEntityAccessor) tropicalFish).invokeSetPackedVariant(id);
    }
}
