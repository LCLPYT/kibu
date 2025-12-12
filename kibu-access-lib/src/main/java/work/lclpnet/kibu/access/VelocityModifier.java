package work.lclpnet.kibu.access;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import work.lclpnet.kibu.access.mixin.EntityAccessor;

public class VelocityModifier {

    private VelocityModifier() {}

    /**
     * Sets the velocity of an entity and schedules a velocity update.
     * @param entity   The entity to modify the velocity of.
     * @param velocity The new velocity of the entity
     */
    public static void setVelocity(Entity entity, Vec3 velocity) {
        entity.setDeltaMovement(velocity);
        ((EntityAccessor) entity).invokeMarkHurt();
    }
}
