package work.lclpnet.kibu.mc;

import work.lclpnet.kibu.jnbt.CompoundTag;

public class BuiltinKibuBlockEntity implements KibuBlockEntity {

    private final String id;
    private final KibuBlockPos position;
    private final CompoundTag data;

    public BuiltinKibuBlockEntity(String id, KibuBlockPos position, CompoundTag data) {
        this.id = id;
        this.position = position;
        this.data = data;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public KibuBlockPos getPosition() {
        return position;
    }

    @Override
    public CompoundTag createNbt() {
        CompoundTag nbt = new CompoundTag();

        for (String key : data.keySet()) {
            nbt.put(key, data.get(key));
        }

        return nbt;
    }
}
