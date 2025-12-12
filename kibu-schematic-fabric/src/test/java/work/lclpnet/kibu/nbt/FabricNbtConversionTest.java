package work.lclpnet.kibu.nbt;

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

        var compound = FabricNbtConversion.convert(tag, net.minecraft.nbt.CompoundTag.class);
        assertEquals(3, compound.keySet().size());
        assertTrue(compound.getBoolean("test").orElseThrow());
        assertEquals("world", compound.getString("hello").orElseThrow());
        assertEquals(5, compound.getInt("c").orElseThrow());
    }

    @Test
    void convertToTag_compound() {
        var compound = new net.minecraft.nbt.CompoundTag();
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

        var list = FabricNbtConversion.convert(tag, net.minecraft.nbt.ListTag.class);
        assertEquals(3, list.size());
        assertEquals(5.1d, list.getDouble(0).orElseThrow());
        assertEquals(0.001d, list.getDouble(1).orElseThrow());
        assertEquals(10.5d, list.getDouble(2).orElseThrow());
    }

    @Test
    void convertToTag_array() {
        var list = new net.minecraft.nbt.ListTag();
        list.add(net.minecraft.nbt.DoubleTag.valueOf(5.1d));
        list.add(net.minecraft.nbt.DoubleTag.valueOf(0.001d));
        list.add(net.minecraft.nbt.DoubleTag.valueOf(10.5d));

        ListTag tag = FabricNbtConversion.convert(list, ListTag.class);
        List<Tag> value = tag.getValue();
        assertEquals(3, value.size());
        assertEquals(5.1d, ((DoubleTag) value.get(0)).getValue());
        assertEquals(0.001d, ((DoubleTag) value.get(1)).getValue());
        assertEquals(10.5d, ((DoubleTag) value.get(2)).getValue());
    }
}