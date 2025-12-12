package work.lclpnet.kibu.access.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {

    @Invoker
    void invokeMarkHurt();

    @Accessor("DATA_SHARED_FLAGS_ID")
    static EntityDataAccessor<Byte> getFlagsTrackedData() {
        throw new AssertionError();
    }

    @Accessor("FLAG_ONFIRE")
    static int getOnFireFlagIndex() {
        throw new AssertionError();
    }

    @Accessor("FLAG_SHIFT_KEY_DOWN")
    static int getSneakingFlagIndex() {
        throw new AssertionError();
    }

    @Accessor("FLAG_SPRINTING")
    static int getSprintingFlagIndex() {
        throw new AssertionError();
    }

    @Accessor("FLAG_SWIMMING")
    static int getSwimmingFlagIndex() {
        throw new AssertionError();
    }

    @Accessor("FLAG_INVISIBLE")
    static int getInvisibleFlagIndex() {
        throw new AssertionError();
    }

    @Accessor("FLAG_GLOWING")
    static int getGlowingFlagIndex() {
        throw new AssertionError();
    }

    @Accessor("FLAG_FALL_FLYING")
    static int getGlidingFlagIndex() {
        throw new AssertionError();
    }
}
