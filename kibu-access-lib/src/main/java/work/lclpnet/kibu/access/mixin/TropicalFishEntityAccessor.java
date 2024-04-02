package work.lclpnet.kibu.access.mixin;

import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TropicalFishEntity.class)
public interface TropicalFishEntityAccessor {

    @Invoker
    static int invokeGetVariantId(TropicalFishEntity.Variety variety, DyeColor baseColor, DyeColor patternColor) {
        throw new AssertionError();
    }

    @Invoker
    void invokeSetTropicalFishVariant(int variant);
}
