package work.lclpnet.kibu.access.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.data.TrackedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {

    @Invoker
    void invokeScheduleVelocityUpdate();

    @Accessor("FLAGS")
    static TrackedData<Byte> getFlagsTrackedData() {
        throw new AssertionError();
    }

    @Accessor("ON_FIRE_FLAG_INDEX")
    static int getOnFireFlagIndex() {
        throw new AssertionError();
    }

    @Accessor("SNEAKING_FLAG_INDEX")
    static int getSneakingFlagIndex() {
        throw new AssertionError();
    }

    @Accessor("SPRINTING_FLAG_INDEX")
    static int getSprintingFlagIndex() {
        throw new AssertionError();
    }

    @Accessor("SWIMMING_FLAG_INDEX")
    static int getSwimmingFlagIndex() {
        throw new AssertionError();
    }

    @Accessor("INVISIBLE_FLAG_INDEX")
    static int getInvisibleFlagIndex() {
        throw new AssertionError();
    }

    @Accessor("GLOWING_FLAG_INDEX")
    static int getGlowingFlagIndex() {
        throw new AssertionError();
    }

    @Accessor("FALL_FLYING_FLAG_INDEX")
    static int getFallFlyingFlagIndex() {
        throw new AssertionError();
    }
}
