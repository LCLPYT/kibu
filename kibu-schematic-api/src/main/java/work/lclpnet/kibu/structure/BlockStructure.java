package work.lclpnet.kibu.structure;

import work.lclpnet.kibu.schematic.api.SchematicWriteable;

public interface BlockStructure extends BlockStorage, BlockEntityStorage, EntityStorage, SchematicWriteable {

    BlockStructure EMPTY = new EmptyStructure();
}
