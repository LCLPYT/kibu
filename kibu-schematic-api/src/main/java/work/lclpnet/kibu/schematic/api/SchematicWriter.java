package work.lclpnet.kibu.schematic.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public interface SchematicWriter {

    void write(SchematicWriteable schematic, OutputStream out) throws IOException;

    default byte[] toArray(SchematicWriteable schematic) throws IOException {
        var out = new ByteArrayOutputStream();
        write(schematic, out);
        return out.toByteArray();
    }
}
