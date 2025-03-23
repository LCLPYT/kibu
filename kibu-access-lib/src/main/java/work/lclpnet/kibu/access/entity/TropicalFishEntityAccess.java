package work.lclpnet.kibu.access.entity;

import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.util.DyeColor;
import work.lclpnet.kibu.access.mixin.TropicalFishEntityAccessor;

public class TropicalFishEntityAccess {

    private TropicalFishEntityAccess() {}

    public static void setVariant(TropicalFishEntity tropicalFish, TropicalFishEntity.Pattern pattern, DyeColor baseColor, DyeColor patternColor) {
        int id = TropicalFishEntityAccessor.invokeGetVariantId(pattern, baseColor, patternColor);
        ((TropicalFishEntityAccessor) tropicalFish).invokeSetTropicalFishVariant(id);
    }
}
