package work.lclpnet.kibu.access;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import work.lclpnet.kibu.access.mixin.EntityAccessor;

public class VelocityModifier {

    private VelocityModifier() {}

    /**
     * Sets the velocity of an entity and schedules a velocity update.
     * @param entity   The entity to modify the velocity of.
     * @param velocity The new velocity of the entity
     */
    public static void setVelocity(Entity entity, Vec3d velocity) {
        entity.setVelocity(velocity);
        ((EntityAccessor) entity).invokeScheduleVelocityUpdate();
    }
}
