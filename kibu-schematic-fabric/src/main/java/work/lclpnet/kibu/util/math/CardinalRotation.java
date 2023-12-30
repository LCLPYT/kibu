package work.lclpnet.kibu.util.math;

/**
 * Represents a cardinal rotation (rotation about the axes by multiples of 90 degrees)
 * @param rotationX Rotation on the x-axis. Will be multiplied with 90 degrees (pi/2 radians).
 * @param rotationY Rotation on the y-axis. Will be multiplied with 90 degrees (pi/2 radians).
 * @param rotationZ Rotation on the Z-axis. Will be multiplied with 90 degrees (pi/2 radians).
 * @param order The order in which to apply the rotations.
 */
public record CardinalRotation(int rotationX, int rotationY, int rotationZ, RotationOrder order) {

    public Matrix3i asMatrix3() {
        Matrix3i first = getRotation(order.first);
        Matrix3i second = getRotation(order.second);
        Matrix3i third = getRotation(order.third);

        return third.multiply(second).multiply(first);
    }

    private Matrix3i getRotation(int i) {
        return switch (i) {
            case 0 -> Matrix3i.makeRotationX(rotationX);
            case 1 -> Matrix3i.makeRotationY(rotationY);
            case 2 -> Matrix3i.makeRotationZ(rotationZ);
            default -> throw new IndexOutOfBoundsException("Invalid rotation index");
        };
    }
}
