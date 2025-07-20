package work.lclpnet.kibu.access.mixin;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.decoration.DisplayEntity;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import work.lclpnet.kibu.access.type.KibuDisplayEntity;

@Mixin(DisplayEntity.class)
public class DisplayEntityMixin implements KibuDisplayEntity {

    @Shadow @Final private static TrackedData<Vector3f> TRANSLATION;

    @Shadow @Final private static TrackedData<Quaternionf> LEFT_ROTATION;

    @Shadow @Final private static TrackedData<Quaternionf> RIGHT_ROTATION;

    @Shadow @Final private static TrackedData<Vector3f> SCALE;

    @Override
    public void kibu$setTranslation(Vector3f translation) {
        ((DisplayEntity) (Object) this).getDataTracker().set(TRANSLATION, translation);
    }

    @Override
    public Vector3f kibu$getTranslation() {
        return ((DisplayEntity) (Object) this).getDataTracker().get(TRANSLATION);
    }

    @Override
    public void kibu$setLeftRotation(Quaternionf leftRotation) {
        ((DisplayEntity) (Object) this).getDataTracker().set(LEFT_ROTATION, leftRotation);
    }

    @Override
    public Quaternionf kibu$getLeftRotation() {
        return ((DisplayEntity) (Object) this).getDataTracker().get(LEFT_ROTATION);
    }

    @Override
    public void kibu$setScale(Vector3f scale) {
        ((DisplayEntity) (Object) this).getDataTracker().set(SCALE, scale);
    }

    @Override
    public Vector3f kibu$getScale() {
        return ((DisplayEntity) (Object) this).getDataTracker().get(SCALE);
    }

    @Override
    public void kibu$setRightRotation(Quaternionf rightRotation) {
        ((DisplayEntity) (Object) this).getDataTracker().set(RIGHT_ROTATION, rightRotation);
    }

    @Override
    public Quaternionf kibu$getRightRotation() {
        return null;
    }
}
