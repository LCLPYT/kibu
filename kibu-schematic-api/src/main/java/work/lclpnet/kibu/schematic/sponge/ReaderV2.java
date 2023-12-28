package work.lclpnet.kibu.schematic.sponge;

import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.jnbt.io.NbtIOHelper;
import work.lclpnet.kibu.mc.BlockStateAdapter;
import work.lclpnet.kibu.schematic.api.BlockStructureFactory;
import work.lclpnet.kibu.schematic.api.SchematicDeserializer;
import work.lclpnet.kibu.schematic.api.SchematicReader;
import work.lclpnet.kibu.structure.BlockStructure;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

class ReaderV2 implements SchematicReader {

    private final SchematicDeserializer deserializer;

    ReaderV2(SchematicDeserializer deserializer) {
        this.deserializer = Objects.requireNonNull(deserializer);
    }

    @Override
    public BlockStructure read(InputStream in, BlockStateAdapter adapter, BlockStructureFactory factory) throws IOException {
        var tag = NbtIOHelper.read(in);
        if (!SpongeSchematicV2.SCHEMATIC.equals(tag.name())) throw new IOException("Invalid nbt");

        var nbt = tag.tag();
        if (!(nbt instanceof CompoundTag compoundNbt)) throw new IOException("Invalid nbt");

        return deserializer.deserialize(compoundNbt, adapter, factory);
    }
}
