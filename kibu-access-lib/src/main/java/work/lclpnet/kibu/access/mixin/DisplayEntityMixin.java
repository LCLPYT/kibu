package work.lclpnet.kibu.access.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Display;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import work.lclpnet.kibu.access.type.KibuDisplayEntity;

@Mixin(Display.class)
public class DisplayEntityMixin implements KibuDisplayEntity {

    @Shadow @Final private static EntityDataAccessor<Vector3f> DATA_TRANSLATION_ID;

    @Shadow @Final private static EntityDataAccessor<Quaternionf> DATA_LEFT_ROTATION_ID;

    @Shadow @Final private static EntityDataAccessor<Quaternionf> DATA_RIGHT_ROTATION_ID;

    @Shadow @Final private static EntityDataAccessor<Vector3f> DATA_SCALE_ID;

    @Override
    public void kibu$setTranslation(Vector3f translation) {
        ((Display) (Object) this).getEntityData().set(DATA_TRANSLATION_ID, translation);
    }

    @Override
    public Vector3f kibu$getTranslation() {
        return ((Display) (Object) this).getEntityData().get(DATA_TRANSLATION_ID);
    }

    @Override
    public void kibu$setLeftRotation(Quaternionf leftRotation) {
        ((Display) (Object) this).getEntityData().set(DATA_LEFT_ROTATION_ID, leftRotation);
    }

    @Override
    public Quaternionf kibu$getLeftRotation() {
        return ((Display) (Object) this).getEntityData().get(DATA_LEFT_ROTATION_ID);
    }

    @Override
    public void kibu$setScale(Vector3f scale) {
        ((Display) (Object) this).getEntityData().set(DATA_SCALE_ID, scale);
    }

    @Override
    public Vector3f kibu$getScale() {
        return ((Display) (Object) this).getEntityData().get(DATA_SCALE_ID);
    }

    @Override
    public void kibu$setRightRotation(Quaternionf rightRotation) {
        ((Display) (Object) this).getEntityData().set(DATA_RIGHT_ROTATION_ID, rightRotation);
    }

    @Override
    public Quaternionf kibu$getRightRotation() {
        return null;
    }
}
