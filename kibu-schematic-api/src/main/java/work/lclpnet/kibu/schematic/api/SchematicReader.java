package work.lclpnet.kibu.schematic.api;

import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.mc.BlockStateAdapter;
import work.lclpnet.kibu.structure.BlockStructure;
import work.lclpnet.kibu.structure.SimpleBlockStructure;

import java.util.function.Function;

public interface SchematicReader {

    BlockStructure read(CompoundTag nbt, BlockStateAdapter blockStateAdapter, Function<Integer, BlockStructure> containerFactory);

    default BlockStructure read(CompoundTag nbt, BlockStateAdapter blockStateAdapter) {
        return read(nbt, blockStateAdapter, SimpleBlockStructure::new);
    }
}
