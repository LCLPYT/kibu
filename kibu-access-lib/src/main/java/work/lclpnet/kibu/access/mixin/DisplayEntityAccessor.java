package work.lclpnet.kibu.access.mixin;

import com.mojang.math.Transformation;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Display.class)
public interface DisplayEntityAccessor {

    @Invoker
    int invokeGetGlowColorOverride();
    @Invoker
    float invokeGetHeight();
    @Invoker
    float invokeGetWidth();
    @Invoker
    float invokeGetShadowStrength();
    @Invoker
    float invokeGetShadowRadius();
    @Invoker
    float invokeGetViewRange();
    @Invoker
    int invokeGetPackedBrightnessOverride();
    @Invoker
    Display.BillboardConstraints invokeGetBillboardConstraints();
    @Invoker
    int invokeGetTransformationInterpolationDelay();
    @Invoker
    int invokeGetTransformationInterpolationDuration();

    @Invoker
    static Transformation invokeCreateTransformation(@SuppressWarnings("unused") SynchedEntityData dataTracker) {
        throw new AssertionError();
    }
}
