package work.lclpnet.kibu.access.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import work.lclpnet.kibu.access.mixin.EntityAccessor;

public class EntityAccess {

    public static final TrackedData<Byte> FLAGS = EntityAccessor.getFlagsTrackedData();
    public static final int ON_FIRE_FLAG_INDEX = EntityAccessor.getOnFireFlagIndex();
    public static final int SNEAKING_FLAG_INDEX = EntityAccessor.getSneakingFlagIndex();
    public static final int SPRINTING_FLAG_INDEX = EntityAccessor.getSprintingFlagIndex();
    public static final int SWIMMING_FLAG_INDEX = EntityAccessor.getSwimmingFlagIndex();
    public static final int INVISIBLE_FLAG_INDEX = EntityAccessor.getInvisibleFlagIndex();
    public static final int GLOWING_FLAG_INDEX = EntityAccessor.getGlowingFlagIndex();
    public static final int GLIDING_FLAG_INDEX = EntityAccessor.getGlidingFlagIndex();

    private EntityAccess() {}

    public static void setFlag(Entity entity, int index, boolean value) {
        DataTracker dataTracker = entity.getDataTracker();

        byte b = dataTracker.get(FLAGS);

        if (value) {
            dataTracker.set(FLAGS, (byte) (b | 1 << index));
        } else {
            dataTracker.set(FLAGS, (byte) (b & ~(1 << index)));
        }
    }

    public static boolean getFlag(Entity entity, int index) {
        DataTracker dataTracker = entity.getDataTracker();
        return (dataTracker.get(FLAGS) & 1 << index) != 0;
    }

    public static byte setFlag(byte flags, int index, boolean value) {
        if (value) {
            return (byte) (flags | 1 << index);
        } else {
            return (byte) (flags & ~(1 << index));
        }
    }
}
