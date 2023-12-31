package work.lclpnet.kibu.util;

import org.joml.Vector3f;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RotationUtilTest {

    @ParameterizedTest
    @MethodSource("rotationVector16")
    void getVector(int rotation, Vector3f expected) {
        Vector3f vector = RotationUtil.getVector(rotation, 16);
        assertEquals(expected.x, vector.x, 0.01);
        assertEquals(expected.y, vector.y, 0.01);
        assertEquals(expected.z, vector.z, 0.01);
    }

    @ParameterizedTest
    @MethodSource("rotationVector16")
    void getRotation(int expected, Vector3f vector) {
        int rotation = RotationUtil.getRotation(vector, 16);
        assertEquals(expected, rotation);
    }

    private static Stream<Arguments> rotationVector16() {
        return Stream.of(
                Arguments.of(0, new Vector3f(0, 0, -1)),
                Arguments.of(1, new Vector3f(0.38f, 0, -0.92f)),
                Arguments.of(2, new Vector3f(0.70f, 0, -0.70f)),
                Arguments.of(3, new Vector3f(0.92f, 0, -0.38f)),
                Arguments.of(4, new Vector3f(1, 0, 0)),
                Arguments.of(5, new Vector3f(0.92f, 0, 0.38f)),
                Arguments.of(6, new Vector3f(0.70f, 0, 0.70f)),
                Arguments.of(7, new Vector3f(0.38f, 0, 0.92f)),
                Arguments.of(8, new Vector3f(0, 0, 1)),
                Arguments.of(9, new Vector3f(-0.38f, 0, 0.92f)),
                Arguments.of(10, new Vector3f(-0.70f, 0, 0.70f)),
                Arguments.of(11, new Vector3f(-0.92f, 0, 0.38f)),
                Arguments.of(12, new Vector3f(-1, 0, 0)),
                Arguments.of(13, new Vector3f(-0.92f, 0, -0.38f)),
                Arguments.of(14, new Vector3f(-0.70f, 0, -0.70f)),
                Arguments.of(15, new Vector3f(-0.38f, 0, -0.92f))
        );
    }
}