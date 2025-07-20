package work.lclpnet.kibu.access.type;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface KibuDisplayEntity {

    void kibu$setTranslation(Vector3f translation);

    Vector3f kibu$getTranslation();

    void kibu$setLeftRotation(Quaternionf leftRotation);

    Quaternionf kibu$getLeftRotation();

    void kibu$setScale(Vector3f scale);

    Vector3f kibu$getScale();

    void kibu$setRightRotation(Quaternionf rightRotation);

    Quaternionf kibu$getRightRotation();
}
