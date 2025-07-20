package work.lclpnet.kibu.access.mixin;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.util.math.AffineTransformation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DisplayEntity.class)
public interface DisplayEntityAccessor {

    @Invoker
    int invokeGetGlowColorOverride();
    @Invoker
    float invokeGetDisplayHeight();
    @Invoker
    float invokeGetDisplayWidth();
    @Invoker
    float invokeGetShadowStrength();
    @Invoker
    float invokeGetShadowRadius();
    @Invoker
    float invokeGetViewRange();
    @Invoker
    int invokeGetBrightness();
    @Invoker
    DisplayEntity.BillboardMode invokeGetBillboardMode();
    @Invoker
    int invokeGetStartInterpolation();
    @Invoker
    int invokeGetInterpolationDuration();

    @Invoker
    static AffineTransformation invokeGetTransformation(@SuppressWarnings("unused") DataTracker dataTracker) {
        throw new AssertionError();
    }
}
