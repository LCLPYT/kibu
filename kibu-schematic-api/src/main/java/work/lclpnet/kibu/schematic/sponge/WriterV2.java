package work.lclpnet.kibu.schematic.sponge;

import work.lclpnet.kibu.jnbt.io.NbtIOHelper;
import work.lclpnet.kibu.schematic.api.SchematicSerializer;
import work.lclpnet.kibu.schematic.api.SchematicWriteable;
import work.lclpnet.kibu.schematic.api.SchematicWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class WriterV2 implements SchematicWriter {

    private final SchematicSerializer serializer;

    public WriterV2(SchematicSerializer serializer) {
        this.serializer = Objects.requireNonNull(serializer);
    }

    @Override
    public void write(SchematicWriteable schematic, OutputStream out) throws IOException {
        var nbt = serializer.serialize(schematic);
        NbtIOHelper.write(nbt, SpongeSchematicV2.SCHEMATIC, out);
    }
}
