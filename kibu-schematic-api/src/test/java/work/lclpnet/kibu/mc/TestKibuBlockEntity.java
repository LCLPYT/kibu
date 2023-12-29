package work.lclpnet.kibu.mc;

import work.lclpnet.kibu.jnbt.CompoundTag;

public record TestKibuBlockEntity(String id, KibuBlockPos pos, CompoundTag nbt) implements KibuBlockEntity {

    @Override
    public String getId() {
        return id;
    }

    @Override
    public KibuBlockPos getPosition() {
        return pos;
    }

    @Override
    public CompoundTag createNbt() {
        return nbt;
    }

    public static TestKibuBlockEntity of(String id, KibuBlockPos pos) {
        return new TestKibuBlockEntity(id, pos, new CompoundTag());
    }
}
