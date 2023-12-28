package work.lclpnet.kibu.schematic.api;

import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.mc.BlockStateAdapter;
import work.lclpnet.kibu.structure.ArrayBlockStructure;
import work.lclpnet.kibu.structure.BlockStructure;

public interface SchematicDeserializer {

    BlockStructure deserialize(CompoundTag nbt, BlockStateAdapter blockStateAdapter, BlockStructureFactory factory);

    default BlockStructure deserialize(CompoundTag nbt, BlockStateAdapter blockStateAdapter) {
        return deserialize(nbt, blockStateAdapter, ArrayBlockStructure::new);
    }
}
