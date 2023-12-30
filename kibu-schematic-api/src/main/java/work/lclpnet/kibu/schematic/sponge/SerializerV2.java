package work.lclpnet.kibu.schematic.sponge;

import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.jnbt.DoubleTag;
import work.lclpnet.kibu.jnbt.ListTag;
import work.lclpnet.kibu.jnbt.NBTConstants;
import work.lclpnet.kibu.mc.KibuBlockEntity;
import work.lclpnet.kibu.mc.KibuBlockPos;
import work.lclpnet.kibu.mc.KibuEntity;
import work.lclpnet.kibu.schematic.api.SchematicSerializer;
import work.lclpnet.kibu.schematic.api.SchematicWriteable;

import javax.annotation.Nonnull;
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
                        CompoundTag blockEntityNbt = getBlockEntityNbt(blockEntity, x, y, z);
                        blockEntities.add(blockEntityNbt);
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

        final var entitiesNbt = getEntitiesNbt(schematic);
        nbt.put(ENTITIES, entitiesNbt);

        return nbt;
    }

    @Nonnull
    private static CompoundTag getBlockEntityNbt(KibuBlockEntity blockEntity, int x, int y, int z) {
        final var nbt = blockEntity.createNbt();

        final var blockEntityId = blockEntity.getId();
        nbt.putString(BLOCK_ENTITY_ID, blockEntityId);

        nbt.putIntArray(BLOCK_ENTITY_POS, new int[]{x, y, z});

        nbt.remove("id");
        nbt.remove("x");
        nbt.remove("y");
        nbt.remove("z");

        return nbt;
    }

    private static ListTag getEntitiesNbt(SchematicWriteable schematic) {
        final var nbt = new ListTag(CompoundTag.class);

        for (KibuEntity entity : schematic.getEntities()) {
            CompoundTag entityNbt = getEntityNbt(entity);
            nbt.add(entityNbt);
        }

        return nbt;
    }

    private static CompoundTag getEntityNbt(KibuEntity entity) {
        CompoundTag nbt = new CompoundTag();

        CompoundTag extraNbt = entity.getExtraNbt();

        for (String key : extraNbt.keySet()) {
            nbt.put(key, extraNbt.get(key));
        }

        // clean conflicting vanilla tags
        nbt.remove("Pos");
        nbt.remove("id");

        nbt.putString(ENTITY_ID, entity.getId());

        ListTag pos = new ListTag(NBTConstants.TYPE_DOUBLE);
        pos.add(new DoubleTag(entity.getX()));
        pos.add(new DoubleTag(entity.getY()));
        pos.add(new DoubleTag(entity.getZ()));

        nbt.put(ENTITY_POS, pos);

        return nbt;
    }
}
