package work.lclpnet.kibu.util.math;

import net.minecraft.util.math.Vec3i;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Matrix3iTest {

    @Test
    void identity() {
        Matrix3i identity = new Matrix3i();
        assertEquals(new Vec3i(1, 2, 3), identity.transform(1, 2, 3));
        assertEquals(new Vec3i(-1, -2, -3), identity.transform(-1, -2, -3));
    }

    @Test
    void testRotateYBy0() {
        Matrix3i rotation = Matrix3i.makeRotationY(0);
        assertEquals(new Matrix3i(), rotation);
    }

    @Test
    void testRotateYBy90() {
        Matrix3i rotation = Matrix3i.makeRotationY(1);
        assertEquals(new Vec3i(1, 0, -1), rotation.transform(1, 0, 1));
        assertEquals(new Vec3i(1, 0, 1), rotation.transform(-1, 0, 1));
        assertEquals(new Vec3i(-1, 0, -1), rotation.transform(1, 0, -1));
        assertEquals(new Vec3i(-1, 0, 1), rotation.transform(-1, 0, -1));
    }

    @Test
    void testRotateYBy180() {
        Matrix3i rotation = Matrix3i.makeRotationY(2);
        assertEquals(new Vec3i(-1, 0, -1), rotation.transform(1, 0, 1));
        assertEquals(new Vec3i(1, 0, -1), rotation.transform(-1, 0, 1));
        assertEquals(new Vec3i(-1, 0, 1), rotation.transform(1, 0, -1));
        assertEquals(new Vec3i(1, 0, 1), rotation.transform(-1, 0, -1));
    }

    @Test
    void testRotateYBy270() {
        Matrix3i rotation = Matrix3i.makeRotationY(3);
        assertEquals(new Vec3i(-1, 0, 1), rotation.transform(1, 0, 1));
        assertEquals(new Vec3i(-1, 0, -1), rotation.transform(-1, 0, 1));
        assertEquals(new Vec3i(1, 0, 1), rotation.transform(1, 0, -1));
        assertEquals(new Vec3i(1, 0, -1), rotation.transform(-1, 0, -1));
    }

    @Test
    void testRotateYBy360() {
        Matrix3i rotation = Matrix3i.makeRotationY(4);
        assertEquals(Matrix3i.IDENTITY, rotation);
    }

    @Test
    void testRotateZBy0() {
        Matrix3i rotation = Matrix3i.makeRotationZ(0);
        assertEquals(new Matrix3i(), rotation);
    }

    @Test
    void testRotateZBy90() {
        Matrix3i rotation = Matrix3i.makeRotationZ(1);
        assertEquals(new Vec3i(-1, 1, 0), rotation.transform(1, 1, 0));
        assertEquals(new Vec3i(-1, -1, 0), rotation.transform(-1, 1, 0));
        assertEquals(new Vec3i(1, 1, 0), rotation.transform(1, -1, 0));
        assertEquals(new Vec3i(1, -1, 0), rotation.transform(-1, -1, 0));
    }

    @Test
    void testRotateZBy180() {
        Matrix3i rotation = Matrix3i.makeRotationZ(2);
        assertEquals(new Vec3i(-1, -1, 0), rotation.transform(1, 1, 0));
        assertEquals(new Vec3i(1, -1, 0), rotation.transform(-1, 1, 0));
        assertEquals(new Vec3i(-1, 1, 0), rotation.transform(1, -1, 0));
        assertEquals(new Vec3i(1, 1, 0), rotation.transform(-1, -1, 0));
    }

    @Test
    void testRotateZBy270() {
        Matrix3i rotation = Matrix3i.makeRotationZ(3);
        assertEquals(new Vec3i(1, -1, 0), rotation.transform(1, 1, 0));
        assertEquals(new Vec3i(1, 1, 0), rotation.transform(-1, 1, 0));
        assertEquals(new Vec3i(-1, -1, 0), rotation.transform(1, -1, 0));
        assertEquals(new Vec3i(-1, 1, 0), rotation.transform(-1, -1, 0));
    }

    @Test
    void testRotateZBy360() {
        Matrix3i rotation = Matrix3i.makeRotationZ(4);
        assertEquals(Matrix3i.IDENTITY, rotation);
    }

    @Test
    void testRotateXBy0() {
        Matrix3i rotation = Matrix3i.makeRotationX(0);
        assertEquals(new Matrix3i(), rotation);
    }

    @Test
    void testRotateXBy90() {
        Matrix3i rotation = Matrix3i.makeRotationX(1);
        assertEquals(new Vec3i(0, -1, 1), rotation.transform(0, 1, 1));
        assertEquals(new Vec3i(0, -1, -1), rotation.transform(0, -1, 1));
        assertEquals(new Vec3i(0, 1, 1), rotation.transform(0, 1, -1));
        assertEquals(new Vec3i(0, 1, -1), rotation.transform(0, -1, -1));
    }

    @Test
    void testRotateXBy180() {
        Matrix3i rotation = Matrix3i.makeRotationX(2);
        assertEquals(new Vec3i(0, -1, -1), rotation.transform(0, 1, 1));
        assertEquals(new Vec3i(0, 1, -1), rotation.transform(0, -1, 1));
        assertEquals(new Vec3i(0, -1, 1), rotation.transform(0, 1, -1));
        assertEquals(new Vec3i(0, 1, 1), rotation.transform(0, -1, -1));
    }

    @Test
    void testRotateXBy270() {
        Matrix3i rotation = Matrix3i.makeRotationX(3);
        assertEquals(new Vec3i(0, 1, -1), rotation.transform(0, 1, 1));
        assertEquals(new Vec3i(0, 1, 1), rotation.transform(0, -1, 1));
        assertEquals(new Vec3i(0, -1, -1), rotation.transform(0, 1, -1));
        assertEquals(new Vec3i(0, -1, 1), rotation.transform(0, -1, -1));
    }

    @Test
    void testRotateXBy360() {
        Matrix3i rotation = Matrix3i.makeRotationX(4);
        assertEquals(Matrix3i.IDENTITY, rotation);
    }

    @Test
    void multiply() {
        Matrix3i rotationZ = Matrix3i.makeRotationZ(1);
        Matrix3i rotationX = Matrix3i.makeRotationX(1);

        assertEquals(new Matrix3i(new int[] {
                0, 0, 1,
                1, 0, 0,
                0, 1, 0
        }), rotationZ.multiply(rotationX));
    }

    @Test
    void multiplySample() {
        int[] elements = {
                1, 2, 3,
                4, 5, 6,
                7, 8, 9
        };

        assertEquals(new Matrix3i(new int[] {
                30, 36, 42,
                66, 81, 96,
                102, 126, 150
        }), new Matrix3i(elements).multiply(new Matrix3i(elements)));
    }

    @Test
    public void invertIdentity() {
        assertEquals(Matrix3i.IDENTITY, Matrix3i.IDENTITY.invert());
    }

    @Test
    public void invertNonSingular() {
        var matrix = new Matrix3i(new int[] {
                1, 2, 3,
                0, 1, 4,
                5, 6, 0
        });

        Matrix3i inverted = matrix.invert();

        assertArrayEquals(new int[] {
                -24, 18, 5,
                20, -15, -4,
                -5, 4, 1
        }, inverted.elements);
    }

    @Test
    public void invertSingular() {
        Matrix3i singularMatrix = new Matrix3i(new int[] {
                2, 4, 6,
                1, 2, 3,
                3, 6, 9
        });

        assertThrows(ArithmeticException.class, singularMatrix::invert);
    }

    @Test
    public void invertRotateX() {
        Matrix3i matrix = Matrix3i.makeRotationX(2);
        Matrix3i inverted = matrix.invert();

        assertEquals(Matrix3i.IDENTITY, matrix.multiply(inverted));
    }

    @Test
    public void invertRotateY() {
        Matrix3i matrix = Matrix3i.makeRotationY(2);
        Matrix3i inverted = matrix.invert();

        assertEquals(Matrix3i.IDENTITY, matrix.multiply(inverted));
    }

    @Test
    public void invertRotateZ() {
        Matrix3i matrix = Matrix3i.makeRotationZ(2);
        Matrix3i inverted = matrix.invert();

        assertEquals(Matrix3i.IDENTITY, matrix.multiply(inverted));
    }
}