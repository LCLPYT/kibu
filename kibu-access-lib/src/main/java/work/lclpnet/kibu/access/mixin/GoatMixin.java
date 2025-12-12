package work.lclpnet.kibu.access.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import work.lclpnet.kibu.access.type.KibuGoatEntity;

@Mixin(Goat.class)
public abstract class GoatMixin extends Animal implements KibuGoatEntity {

    @Shadow @Final private static EntityDataAccessor<Boolean> DATA_HAS_LEFT_HORN;
    @Shadow @Final private static EntityDataAccessor<Boolean> DATA_HAS_RIGHT_HORN;

    protected GoatMixin(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public void kibu$setLeftHorn(boolean exists) {
        entityData.set(DATA_HAS_LEFT_HORN, exists);
    }

    @Override
    public void kibu$setRightHorn(boolean exists) {
        entityData.set(DATA_HAS_RIGHT_HORN, exists);
    }
}
