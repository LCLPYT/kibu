package work.lclpnet.kibu.access.mixin;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.util.math.AffineTransformation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DisplayEntity.class)
public interface DisplayEntityAccessor {

    @Invoker
    void invokeSetGlowColorOverride(int glowColorOverride);
    @Invoker
    int invokeGetGlowColorOverride();
    @Invoker
    void invokeSetDisplayHeight(float height);
    @Invoker
    float invokeGetDisplayHeight();
    @Invoker
    void invokeSetDisplayWidth(float width);
    @Invoker
    float invokeGetDisplayWidth();
    @Invoker
    void invokeSetShadowStrength(float shadowStrength);
    @Invoker
    float invokeGetShadowStrength();
    @Invoker
    void invokeSetShadowRadius(float shadowRadius);
    @Invoker
    float invokeGetShadowRadius();
    @Invoker
    void invokeSetViewRange(float viewRange);
    @Invoker
    float invokeGetViewRange();
    @Invoker
    void invokeSetBrightness(@Nullable Brightness brightness);
    @Invoker
    int invokeGetBrightness();
    @Invoker
    void invokeSetBillboardMode(DisplayEntity.BillboardMode billboardMode);
    @Invoker
    DisplayEntity.BillboardMode invokeGetBillboardMode();
    @Invoker
    void invokeSetStartInterpolation(int startInterpolation);
    @Invoker
    int invokeGetStartInterpolation();
    @Invoker
    void invokeSetInterpolationDuration(int interpolationDuration);
    @Invoker
    int invokeGetInterpolationDuration();
    @Invoker
    void invokeSetTransformation(AffineTransformation transformation);

    @Invoker
    static AffineTransformation invokeGetTransformation(@SuppressWarnings("unused") DataTracker dataTracker) {
        throw new AssertionError();
    }
}
