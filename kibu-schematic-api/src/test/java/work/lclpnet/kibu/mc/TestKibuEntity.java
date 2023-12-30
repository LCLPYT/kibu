package work.lclpnet.kibu.mc;

import work.lclpnet.kibu.jnbt.CompoundTag;

public record TestKibuEntity(String id, double x, double y, double z, CompoundTag extraNbt) implements KibuEntity {

    @Override
    public String getId() {
        return id;
    }

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

    @Override
    public CompoundTag getExtraNbt() {
        return extraNbt;
    }

    public static TestKibuEntity of(String id, double x, double y, double z) {
        return new TestKibuEntity(id, x, y, z, new CompoundTag());
    }
}
