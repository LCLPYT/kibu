package work.lclpnet.kibu.schematic.sponge;

import work.lclpnet.kibu.jnbt.*;
import work.lclpnet.kibu.mc.*;
import work.lclpnet.kibu.schematic.api.BlockStructureFactory;
import work.lclpnet.kibu.schematic.api.SchematicDeserializer;
import work.lclpnet.kibu.schematic.io.VarIntReader;
import work.lclpnet.kibu.structure.BlockStructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static work.lclpnet.kibu.schematic.sponge.SpongeSchematicV2.*;

class DeserializerV2 implements SchematicDeserializer {

    @Override
    public BlockStructure deserialize(CompoundTag nbt, BlockStateAdapter blockStateAdapter, BlockStructureFactory factory) {
        final int version = nbt.getInt(VERSION);
        if (version != FORMAT_VERSION) throw new IllegalArgumentException("Invalid nbt");

        final int dataVersion = nbt.getInt(DATA_VERSION);

        final int width = nbt.getShort(WIDTH);
        final int length = nbt.getShort(LENGTH);
        // height is implicitly defined by the block data buffer length, if needed it can also be read in advance here
        final int height = nbt.getShort(HEIGHT);

        final int[] offset = nbt.getIntArray(OFFSET);
        if (offset.length != 3) throw new IllegalArgumentException("Invalid nbt");

        // parse palette
        final CompoundTag paletteTag = nbt.getCompound(PALETTE);
        final Map<Integer, KibuBlockState> palette = new HashMap<>();

        for (var blockString : paletteTag.keySet()) {
            var blockState = blockStateAdapter.getBlockState(blockString);
            if (blockState == null) continue;

            int id = paletteTag.getInt(blockString);
            palette.put(id, blockState);
        }

        KibuBlockPos origin = new KibuBlockPos(offset[0], offset[1], offset[2]);
        final BlockStructure container = factory.create(width, height, length, origin, dataVersion);

        // parse blocks
        final byte[] blockData = nbt.getByteArray(BLOCK_DATA);
        final var idReader = new VarIntReader(blockData);

        int posIdx = 0;

        while (idReader.hasNext()) {
            int id = idReader.next();

            var state = palette.get(id);
            if (state == null) continue;  // invalid buffer, ignore the error

            // posIdx = (y * length * width) + (z * width) + x
            int y = posIdx / (width * length);
            int z = (posIdx % (width * length)) / width;
            int x = (posIdx % (width * length)) % width;

            var worldPos = new KibuBlockPos(x + offset[0], y + offset[1], z + offset[2]);
            container.setBlockState(worldPos, state);

            posIdx++;
        }

        readBlockEntities(nbt, container, offset);

        readEntities(nbt, container);

        return container;
    }

    private static void readBlockEntities(CompoundTag nbt, BlockStructure container, int[] offset) {
        ListTag blockEntities = nbt.getList(BLOCK_ENTITIES, NBTConstants.TYPE_COMPOUND);

        for (Tag tag : blockEntities) {
            if (!(tag instanceof CompoundTag compoundTag)) continue;

            String id = compoundTag.getString(BLOCK_ENTITY_ID);
            int[] posArray = compoundTag.getIntArray(BLOCK_ENTITY_POS);

            if (posArray.length != 3) continue;

            KibuBlockPos pos = new KibuBlockPos(posArray[0] + offset[0], posArray[1] + offset[1], posArray[2] + offset[2]);
            KibuBlockEntity blockEntity = new BuiltinKibuBlockEntity(id, pos, compoundTag);

            container.setBlockEntity(pos, blockEntity);
        }
    }

    private static void readEntities(CompoundTag nbt, BlockStructure container) {
        ListTag entities = nbt.getList(ENTITIES, NBTConstants.TYPE_COMPOUND);

        for (Tag tag : entities) {
            if (!(tag instanceof CompoundTag compoundTag)) continue;

            String id = compoundTag.getString(ENTITY_ID);
            ListTag posNbt = compoundTag.getList(ENTITY_POS, NBTConstants.TYPE_DOUBLE);
            List<Tag> posList = posNbt.getValue();

            double x, y, z;

            Tag tmp = posList.get(0);
            if (!(tmp instanceof DoubleTag)) continue;
            x = ((DoubleTag) tmp).getValue();

            tmp = posList.get(1);
            if (!(tmp instanceof DoubleTag)) continue;
            y = ((DoubleTag) tmp).getValue();

            tmp = posList.get(2);
            if (!(tmp instanceof DoubleTag)) continue;
            z = ((DoubleTag) tmp).getValue();

            KibuEntity entity = new BuiltinKibuEntity(id, x, y, z, compoundTag);
            container.addEntity(entity);
        }
    }
}
