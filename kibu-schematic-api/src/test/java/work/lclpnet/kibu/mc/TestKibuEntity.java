package work.lclpnet.kibu.mc;

import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.jnbt.DoubleTag;
import work.lclpnet.kibu.jnbt.ListTag;
import work.lclpnet.kibu.jnbt.NBTConstants;

public class TestKibuEntity implements KibuEntity {

    private final CompoundTag nbt;

    public TestKibuEntity(String id, double x, double y, double z) {
        this(id, x, y, z, new CompoundTag());
    }

    public TestKibuEntity(String id, double x, double y, double z, CompoundTag extraNbt) {
        CompoundTag nbt = new CompoundTag();

        for (String s : extraNbt.keySet()) {
            nbt.put(s, extraNbt.get(s));
        }

        nbt.putString("Id", id);

        ListTag pos = new ListTag(NBTConstants.TYPE_DOUBLE);
        pos.add(new DoubleTag(x));
        pos.add(new DoubleTag(y));
        pos.add(new DoubleTag(z));

        nbt.put("Pos", pos);

        this.nbt = nbt;
    }

    @Override
    public CompoundTag createNbt() {
        return nbt;
    }
}
