package work.lclpnet.kibu.nbt;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.jnbt.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FabricNbtConversionTest {

    @Test
    void convertToFabric_compound() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("test", true);
        tag.putString("hello", "world");
        tag.putInt("c", 5);

        NbtCompound compound = FabricNbtConversion.convert(tag, NbtCompound.class);
        assertEquals(3, compound.getKeys().size());
        assertTrue(compound.getBoolean("test"));
        assertEquals("world", compound.getString("hello"));
        assertEquals(5, compound.getInt("c"));
    }

    @Test
    void convertToTag_compound() {
        NbtCompound compound = new NbtCompound();
        compound.putBoolean("test", true);
        compound.putString("hello", "world");
        compound.putInt("c", 5);

        CompoundTag tag = FabricNbtConversion.convert(compound, CompoundTag.class);
        assertEquals(3, tag.keySet().size());
        assertTrue(tag.getBoolean("test"));
        assertEquals("world", tag.getString("hello"));
        assertEquals(5, tag.getInt("c"));
    }

    @Test
    void convertToFabric_array() {
        ListTag tag = new ListTag(NBTConstants.TYPE_DOUBLE);
        tag.add(new DoubleTag(5.1d));
        tag.add(new DoubleTag(0.001d));
        tag.add(new DoubleTag(10.5d));

        NbtList list = FabricNbtConversion.convert(tag, NbtList.class);
        assertEquals(3, list.size());
        assertEquals(5.1d, list.getDouble(0));
        assertEquals(0.001d, list.getDouble(1));
        assertEquals(10.5d, list.getDouble(2));
    }

    @Test
    void convertToTag_array() {
        NbtList list = new NbtList();
        list.add(NbtDouble.of(5.1d));
        list.add(NbtDouble.of(0.001d));
        list.add(NbtDouble.of(10.5d));

        ListTag tag = FabricNbtConversion.convert(list, ListTag.class);
        List<Tag> value = tag.getValue();
        assertEquals(3, value.size());
        assertEquals(5.1d, ((DoubleTag) value.get(0)).getValue());
        assertEquals(0.001d, ((DoubleTag) value.get(1)).getValue());
        assertEquals(10.5d, ((DoubleTag) value.get(2)).getValue());
    }
}