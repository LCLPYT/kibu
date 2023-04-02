package work.lclpnet.kibu.schematic.api;

import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.mc.BlockStateAdapter;
import work.lclpnet.kibu.structure.BlockStructure;
import work.lclpnet.kibu.structure.SimpleBlockStructure;

import java.util.function.Function;

public interface SchematicDeserializer {

    BlockStructure deserialize(CompoundTag nbt, BlockStateAdapter blockStateAdapter, Function<Integer, BlockStructure> containerFactory);

    default BlockStructure deserialize(CompoundTag nbt, BlockStateAdapter blockStateAdapter) {
        return deserialize(nbt, blockStateAdapter, SimpleBlockStructure::new);
    }
}
