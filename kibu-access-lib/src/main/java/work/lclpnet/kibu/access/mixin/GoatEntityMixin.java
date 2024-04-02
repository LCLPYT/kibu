package work.lclpnet.kibu.access.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import work.lclpnet.kibu.access.type.KibuGoatEntity;

@Mixin(GoatEntity.class)
public abstract class GoatEntityMixin extends AnimalEntity implements KibuGoatEntity {

    @Shadow @Final private static TrackedData<Boolean> LEFT_HORN;
    @Shadow @Final private static TrackedData<Boolean> RIGHT_HORN;

    protected GoatEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void kibu$setLeftHorn(boolean exists) {
        dataTracker.set(LEFT_HORN, exists);
    }

    @Override
    public void kibu$setRightHorn(boolean exists) {
        dataTracker.set(RIGHT_HORN, exists);
    }
}
