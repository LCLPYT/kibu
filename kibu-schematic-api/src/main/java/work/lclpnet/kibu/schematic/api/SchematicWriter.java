package work.lclpnet.kibu.schematic.api;

import work.lclpnet.kibu.jnbt.CompoundTag;

public interface SchematicWriter {

    CompoundTag write(SchematicWriteable adapter);
}
