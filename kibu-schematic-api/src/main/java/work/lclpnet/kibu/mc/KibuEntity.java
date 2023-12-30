package work.lclpnet.kibu.mc;

import work.lclpnet.kibu.jnbt.CompoundTag;

public interface KibuEntity {

    String getId();

    double getX();

    double getY();

    double getZ();

    CompoundTag getExtraNbt();
}
