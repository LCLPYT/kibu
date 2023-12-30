package work.lclpnet.kibu.mc;

import work.lclpnet.kibu.jnbt.CompoundTag;

public class TestKibuBlockEntity implements KibuBlockEntity {

    private final CompoundTag nbt;

    public TestKibuBlockEntity(String id, KibuBlockPos pos) {
        this(id, pos, new CompoundTag());
    }

    public TestKibuBlockEntity(String id, KibuBlockPos pos, CompoundTag extraNbt) {
        CompoundTag nbt = new CompoundTag();

        for (String s : extraNbt.keySet()) {
            nbt.put(s, extraNbt.get(s));
        }

        nbt.putString("Id", id);
        nbt.putIntArray("Pos", new int[] { pos.getX(), pos.getY(), pos.getZ() });

        this.nbt = nbt;
    }

    @Override
    public CompoundTag createNbt() {
        return nbt;
    }
}
