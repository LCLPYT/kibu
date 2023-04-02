package work.lclpnet.kibu.schematic.api;

import work.lclpnet.kibu.mc.BlockStateAdapter;
import work.lclpnet.kibu.structure.BlockStructure;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public interface SchematicReader {

    BlockStructure read(InputStream in, BlockStateAdapter adapter) throws IOException;

    default BlockStructure fromArray(byte[] bytes, BlockStateAdapter adapter) throws IOException {
        return read(new ByteArrayInputStream(bytes), adapter);
    }
}
