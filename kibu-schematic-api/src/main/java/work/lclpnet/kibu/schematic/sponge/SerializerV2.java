package work.lclpnet.kibu.schematic.sponge;

import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.jnbt.ListTag;
import work.lclpnet.kibu.mc.KibuBlockPos;
import work.lclpnet.kibu.schematic.api.SchematicSerializer;
import work.lclpnet.kibu.schematic.api.SchematicWriteable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static work.lclpnet.kibu.schematic.sponge.SpongeSchematicV2.*;

class SerializerV2 implements SchematicSerializer {

    @Override
    public CompoundTag serialize(SchematicWriteable schematic) {
        final var offset = schematic.getOrigin();

        final int width = schematic.getWidth();
        final int height = schematic.getHeight();
        final int length = schematic.getLength();

        final var dataBuffer = new ByteArrayOutputStream(width * height * length);
        final var palette = new HashMap<String, Integer>();
        final var blockEntities = new ArrayList<CompoundTag>();
        final var pos = new KibuBlockPos.Mutable();

        int nextId = 0;

        for (int y = 0; y < height; y++) {
            final int worldY = offset.getY() + y;

            for (int z = 0; z < length; z++) {
                final int worldZ = offset.getZ() + z;

                for (int x = 0; x < width; x++) {
                    final int worldX = offset.getX() + x;

                    pos.set(worldX, worldY, worldZ);

                    final var blockString = schematic.getBlockState(pos).getAsString();

                    int id;
                    if (palette.containsKey(blockString)) {
                        id = palette.get(blockString);
                    } else {
                        palette.put(blockString, id = nextId++);
                    }

                    final var blockEntity = schematic.getBlockEntity(pos);
                    if (blockEntity != null) {
                        final var blockEntityNbt = blockEntity.createNbt();

                        if (blockEntityNbt.contains("id")) {
                            final var blockEntityId = blockEntityNbt.getString("id");
                            blockEntityNbt.putString(BLOCK_ENTITY_ID, blockEntityId);

                            blockEntityNbt.putIntArray(BLOCK_ENTITY_POS, new int[]{x, y, z});

                            blockEntityNbt.remove("id");
                            blockEntityNbt.remove("x");
                            blockEntityNbt.remove("y");
                            blockEntityNbt.remove("z");

                            blockEntities.add(blockEntityNbt);
                        }
                    }

                    // write as byte
                    while ((id & -128) != 0) {
                        dataBuffer.write(id & 127 | 128);
                        id >>>= 7;
                    }

                    dataBuffer.write(id);
                }
            }
        }

        final var nbt = new CompoundTag();

        nbt.putInt(VERSION, FORMAT_VERSION);
        nbt.putInt(DATA_VERSION, schematic.getDataVersion());

        nbt.putShort(WIDTH, (short) width);
        nbt.putShort(HEIGHT, (short) height);
        nbt.putShort(LENGTH, (short) length);

        nbt.putIntArray(OFFSET, new int[]{offset.getX(), offset.getY(), offset.getZ()});

        nbt.putInt(PALETTE_MAX, nextId);

        final var paletteNbt = new CompoundTag();
        palette.forEach(paletteNbt::putInt);

        nbt.put(PALETTE, paletteNbt);
        nbt.putByteArray(BLOCK_DATA, dataBuffer.toByteArray());

        final var blockEntityNbt = new ListTag(CompoundTag.class);
        blockEntityNbt.addAll(blockEntities);
        nbt.put(BLOCK_ENTITIES, blockEntityNbt);

        return nbt;
    }
}
