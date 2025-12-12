package work.lclpnet.kibu.access.mixin;

import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TropicalFish.class)
public interface TropicalFishAccessor {

    @Invoker
    static int invokePackVariant(TropicalFish.Pattern pattern, DyeColor baseColor, DyeColor patternColor) {
        throw new AssertionError();
    }

    @Invoker
    void invokeSetPackedVariant(int variant);
}
