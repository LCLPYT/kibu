package work.lclpnet.kibu.access.entity;

import net.minecraft.world.entity.animal.fish.TropicalFish;
import net.minecraft.world.item.DyeColor;
import work.lclpnet.kibu.access.mixin.TropicalFishAccessor;

public class TropicalFishEntityAccess {

    private TropicalFishEntityAccess() {}

    public static void setVariant(TropicalFish tropicalFish, TropicalFish.Pattern pattern, DyeColor baseColor, DyeColor patternColor) {
        int id = TropicalFishAccessor.invokePackVariant(pattern, baseColor, patternColor);
        ((TropicalFishAccessor) tropicalFish).invokeSetPackedVariant(id);
    }
}
