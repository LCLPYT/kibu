package work.lclpnet.kibu.schematic.api;

import work.lclpnet.kibu.jnbt.CompoundTag;

public interface SchematicSerializer {

    CompoundTag serialize(SchematicWriteable schematic);
}
