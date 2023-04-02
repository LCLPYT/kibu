package work.lclpnet.kibu.schematic.sponge;

import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.mc.BlockPos;
import work.lclpnet.kibu.mc.BlockState;
import work.lclpnet.kibu.mc.BlockStateAdapter;
import work.lclpnet.kibu.schematic.api.SchematicDeserializer;
import work.lclpnet.kibu.schematic.io.VarIntReader;
import work.lclpnet.kibu.structure.BlockStructure;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static work.lclpnet.kibu.schematic.sponge.SpongeSchematicV2.*;

class DeserializerV2 implements SchematicDeserializer {

    @Override
    public BlockStructure deserialize(CompoundTag nbt, BlockStateAdapter blockStateAdapter, Function<Integer, BlockStructure> containerFactory) {
        final int version = nbt.getInt(VERSION);
        if (version != FORMAT_VERSION) throw new IllegalArgumentException("Invalid nbt");

        final int dataVersion = nbt.getInt(DATA_VERSION);

        final int width = nbt.getShort(WIDTH);
        final int length = nbt.getShort(LENGTH);
        // height is implicitly defined by the block data buffer length, if needed it can also be read in advance here

        final int[] offset = nbt.getIntArray(OFFSET);
        if (offset.length != 3) throw new IllegalArgumentException("Invalid nbt");

        // parse palette
        final CompoundTag paletteTag = nbt.getCompound(PALETTE);
        final Map<Integer, BlockState> palette = new HashMap<>();

        for (var blockString : paletteTag.keySet()) {
            var blockState = blockStateAdapter.getBlockState(blockString);
            if (blockState == null) continue;

            int id = paletteTag.getInt(blockString);
            palette.put(id, blockState);
        }

        final var container = containerFactory.apply(dataVersion);

        // parse blocks
        final byte[] blockData = nbt.getByteArray(BLOCK_DATA);
        final var worldPos = new BlockPos();
        final var idReader = new VarIntReader(blockData);

        int posIdx = 0;

        while (idReader.hasNext()) {
            int id = idReader.next();

            // posIdx = (y * length * width) + (z * width) + x
            int y = posIdx / (width * length);
            int z = (posIdx % (width * length)) / width;
            int x = (posIdx % (width * length)) % width;

            var state = palette.get(id);
            if (state == null) continue;  // invalid buffer, ignore the error

            worldPos.set(x + offset[0], y + offset[1], z + offset[2]);

            // TODO read block entity, if there is one
            container.setBlockState(worldPos, state);

            posIdx++;
        }

        return container;
    }
}
