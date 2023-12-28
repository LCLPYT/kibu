package work.lclpnet.kibu.schematic.api;

import work.lclpnet.kibu.mc.BlockStateAdapter;
import work.lclpnet.kibu.structure.ArrayBlockStructure;
import work.lclpnet.kibu.structure.BlockStructure;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public interface SchematicReader {

    BlockStructure read(InputStream in, BlockStateAdapter adapter, BlockStructureFactory factory) throws IOException;

    default BlockStructure read(InputStream in, BlockStateAdapter adapter) throws IOException {
        return read(in, adapter, ArrayBlockStructure::new);
    }

    default BlockStructure fromArray(byte[] bytes, BlockStateAdapter adapter) throws IOException {
        return read(new ByteArrayInputStream(bytes), adapter);
    }
}
