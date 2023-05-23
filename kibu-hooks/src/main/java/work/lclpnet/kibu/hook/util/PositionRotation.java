package work.lclpnet.kibu.hook.util;

import net.minecraft.util.math.Position;

public class PositionRotation implements Position {

    protected double x, y, z;
    protected float yaw, pitch;

    public PositionRotation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    private PositionRotation() {}

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public double squaredDistanceTo(PositionRotation other) {
        return Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2) + Math.pow(z - other.z, 2);
    }

    public float angleTo(PositionRotation other) {
        return Math.abs(yaw - other.yaw) + Math.abs(pitch - other.pitch);
    }

    public boolean isDifferentPosition(PositionRotation other) {
        return squaredDistanceTo(other) > 1e-9d;
    }

    public boolean isDifferentRotation(PositionRotation positionRotation) {
        return angleTo(positionRotation) > 1e-6f;
    }
}
