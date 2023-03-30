package work.lclpnet.kibu.schematic.api;

import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.mc.BlockStateAdapter;
import work.lclpnet.kibu.structure.BlockStructure;

public interface SchematicReader {

    BlockStructure read(CompoundTag nbt, BlockStateAdapter blockStateAdapter);
}
