package work.lclpnet.kibu.mc;

import work.lclpnet.kibu.jnbt.CompoundTag;

public record TestKibuBlockEntity(String id, KibuBlockPos pos, CompoundTag extraNbt) implements KibuBlockEntity {

    @Override
    public CompoundTag createNbt() {
        CompoundTag nbt = new CompoundTag();

        for (String s : extraNbt.keySet()) {
            nbt.put(s, extraNbt.get(s));
        }

        nbt.putString("Id", id);
        nbt.putIntArray("Pos", new int[] { pos.getX(), pos.getY(), pos.getZ() });

        return nbt;
    }

    public static TestKibuBlockEntity of(String id, KibuBlockPos pos) {
        return new TestKibuBlockEntity(id, pos, new CompoundTag());
    }
}
