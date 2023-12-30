package work.lclpnet.kibu.mc;

import work.lclpnet.kibu.jnbt.CompoundTag;

public interface KibuBlockEntity {

    String getId();

    KibuBlockPos getPosition();

    CompoundTag createNbt();
}
