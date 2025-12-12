package work.lclpnet.kibu.hook.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Position;
import net.minecraft.world.phys.Vec3;

public class PositionRotation implements Position {

    public static final Codec<PositionRotation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Vec3.CODEC.fieldOf("pos").forGetter(it -> new Vec3(it.x, it.y, it.z)),
            Codec.FLOAT.fieldOf("yaw").forGetter(it -> it.yaw),
            Codec.FLOAT.fieldOf("pitch").forGetter(it -> it.pitch)
    ).apply(instance, (pos, yaw, pitch) -> new PositionRotation(pos.x, pos.y, pos.z, yaw, pitch)));

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
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
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
        return squaredDistanceTo(other) > 1e-3d;
    }

    public boolean isDifferentRotation(PositionRotation positionRotation) {
        return angleTo(positionRotation) > 1e-6f;
    }
}
