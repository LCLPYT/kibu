package work.lclpnet.kibu.schematic.sponge;

import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.jnbt.ListTag;
import work.lclpnet.kibu.jnbt.NBTConstants;
import work.lclpnet.kibu.jnbt.Tag;
import work.lclpnet.kibu.jnbt.io.NbtIOHelper;
import work.lclpnet.kibu.mc.BuiltinKibuBlockEntity;
import work.lclpnet.kibu.mc.BuiltinKibuBlockState;
import work.lclpnet.kibu.mc.BuiltinKibuEntity;
import work.lclpnet.kibu.mc.KibuBlockPos;
import work.lclpnet.kibu.structure.SimpleBlockStructure;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WriterV2Test {

    @Test
    public void testWrite() throws IOException {
        var structure = new SimpleBlockStructure(123);
        var serializer = new SerializerV2();
        var writer = new WriterV2(serializer);

        var bytes = writer.toArray(structure);
        var nbt = NbtIOHelper.fromArray(bytes);
        assertEquals(SpongeSchematicV2.SCHEMATIC, nbt.name());

        var tag = nbt.tag();
        assertTrue(tag instanceof CompoundTag);
        assertEquals(11, ((CompoundTag) tag).keySet().size());
    }

    @Test
    public void testWriteBlockEntity() throws IOException {
        var structure = new SimpleBlockStructure(123);

        KibuBlockPos pos = new KibuBlockPos(10, 5, 11);
        structure.setBlockState(pos, new BuiltinKibuBlockState("minecraft:chest"));

        CompoundTag chestNbt = new CompoundTag();
        chestNbt.put("Items", new ListTag());

        structure.setBlockEntity(pos, new BuiltinKibuBlockEntity("minecraft:chest", pos, chestNbt));

        var serializer = new SerializerV2();
        var writer = new WriterV2(serializer);

        var bytes = writer.toArray(structure);
        var nbt = NbtIOHelper.fromArray(bytes);
        assertEquals(SpongeSchematicV2.SCHEMATIC, nbt.name());

        var tag = nbt.tag();
        assertTrue(tag instanceof CompoundTag);

        CompoundTag chest = (CompoundTag) ((CompoundTag) tag).getList("BlockEntities").getValue().stream()
                .filter(s -> s instanceof CompoundTag beTag && beTag.getString("Id").equals("minecraft:chest"))
                .findAny().orElseThrow();

        assertArrayEquals(new int[] {0, 0, 0}, chest.getIntArray("Pos"));  // relative position
        assertEquals(new ListTag(), chest.getList("Items"));
    }

    @Test
    public void testWriteEntity() throws IOException {
        var structure = new SimpleBlockStructure(123);

        CompoundTag paintingNbt = new CompoundTag();
        paintingNbt.putString("variant", "minecraft:graham");
        structure.addEntity(new BuiltinKibuEntity("minecraft:painting", 10, 5, 3, paintingNbt));

        var serializer = new SerializerV2();
        var writer = new WriterV2(serializer);

        var bytes = writer.toArray(structure);
        var nbt = NbtIOHelper.fromArray(bytes);
        assertEquals(SpongeSchematicV2.SCHEMATIC, nbt.name());

        var tag = nbt.tag();
        assertTrue(tag instanceof CompoundTag);

        CompoundTag painting = (CompoundTag) ((CompoundTag) tag).getList("Entities").getValue().stream()
                .filter(s -> s instanceof CompoundTag beTag && beTag.getString("Id").equals("minecraft:painting"))
                .findAny().orElseThrow();

        List<Tag> list = painting.getList("Pos", NBTConstants.TYPE_DOUBLE).getValue();
        assertEquals(10.0, list.get(0).getValue());
        assertEquals(5.0, list.get(1).getValue());
        assertEquals(3.0, list.get(2).getValue());
        assertEquals("minecraft:graham", painting.getString("variant"));
    }
}
