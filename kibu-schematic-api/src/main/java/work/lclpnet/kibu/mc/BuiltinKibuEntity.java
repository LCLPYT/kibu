package work.lclpnet.kibu.mc;

import work.lclpnet.kibu.jnbt.CompoundTag;

import java.util.Objects;

public class BuiltinKibuEntity implements KibuEntity {

    private final String id;
    private final double x, y, z;
    private final CompoundTag extraNbt;

    public BuiltinKibuEntity(String id, double x, double y, double z, CompoundTag extraNbt) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.extraNbt = extraNbt;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuiltinKibuEntity that = (BuiltinKibuEntity) o;
        return Double.compare(x, that.x) == 0 && Double.compare(y, that.y) == 0 && Double.compare(z, that.z) == 0 && Objects.equals(id, that.id) && Objects.equals(extraNbt, that.extraNbt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, z, extraNbt);
    }
}
