package work.lclpnet.kibu.util.math;

import net.minecraft.util.math.Vec3i;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardinalRotationTest {

    @Test
    void asMatrix3_YZX() {
        var rotation = new CardinalRotation(1, 2, 1, RotationOrder.YZX);
        var mat3 = rotation.asMatrix3();

        assertEquals(new Vec3i(-1, 1, -1), mat3.transform(new Vec3i(1, 1, 1)));
    }
}