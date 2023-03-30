package work.lclpnet.kibu.jnbt;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CompoundTagTest {

    @Test
    void testInit() {
        var nbt = new CompoundTag();
        assertEquals(Collections.emptySet(), nbt.keySet());
        assertEquals(new HashMap<>(), nbt.getValue());
    }

    @Test
    void testBool() {
        var nbt = new CompoundTag();

        assertFalse(nbt.getBoolean("bool"));
        nbt.putBoolean("bool", true);
        assertTrue(nbt.getBoolean("bool"));
    }

    @Test
    void testFloat() {
        var nbt = new CompoundTag();

        assertSimilar(0.0f, nbt.getFloat("float"));
        nbt.putFloat("float", 1.2f);
        assertSimilar(1.2f, nbt.getFloat("float"));
    }

    @Test
    void testDouble() {
        var nbt = new CompoundTag();

        assertSimilar(0.0f, nbt.getDouble("double"));
        nbt.putDouble("double", Math.PI);
        assertSimilar(Math.PI, nbt.getDouble("double"));
    }

    @Test
    void testByteArray() {
        var nbt = new CompoundTag();

        assertArrayEquals(new byte[0], nbt.getByteArray("bytes"));
        final var bytes = new byte[] {2, 3, 5, 127, 6};
        nbt.putByteArray("bytes", bytes);
        assertArrayEquals(bytes, nbt.getByteArray("bytes"));
    }

    @Test
    void testInt() {
        var nbt = new CompoundTag();

        assertEquals(0, nbt.getInt("int"));
        nbt.putInt("int", 11);
        assertEquals(11, nbt.getInt("int"));
    }

    @Test
    void testShort() {
        var nbt = new CompoundTag();

        assertEquals(0, nbt.getShort("short"));
        nbt.putShort("short", (short) 25565);
        assertEquals(25565, nbt.getShort("short"));
    }

    @Test
    void testLong() {
        var nbt = new CompoundTag();

        assertEquals(0, nbt.getLong("long"));
        nbt.putLong("long", 123123L);
        assertEquals(123123, nbt.getLong("long"));
    }

    @Test
    void testByte() {
        var nbt = new CompoundTag();

        assertEquals(0, nbt.getByte("byte"));
        nbt.putByte("byte", (byte) 54);
        assertEquals(54, nbt.getByte("byte"));
    }

    @Test
    void testString() {
        var nbt = new CompoundTag();

        assertEquals("", nbt.getString("string"));
        nbt.putString("string", "hello world");
        assertEquals("hello world", nbt.getString("string"));
    }

    @Test
    void testLongArray() {
        var nbt = new CompoundTag();

        assertArrayEquals(new long[0], nbt.getLongArray("longs"));
        final var longs = new long[] {123, 533, Long.MAX_VALUE, 0, Long.MIN_VALUE, 717171231232L};
        nbt.putLongArray("longs", longs);
        assertArrayEquals(longs, nbt.getLongArray("longs"));
    }

    @Test
    void testIntArray() {
        var nbt = new CompoundTag();

        assertArrayEquals(new int[0], nbt.getIntArray("ints"));
        final var ints = new int[] {123, Integer.MAX_VALUE, 0, Integer.MIN_VALUE, 88331};
        nbt.putIntArray("ints", ints);
        assertArrayEquals(ints, nbt.getIntArray("ints"));
    }

    @Test
    void testUuid() {
        var nbt = new CompoundTag();

        assertThrows(IllegalArgumentException.class, () -> nbt.getUuid("uuid"));
        final var uuid = UUID.randomUUID();
        nbt.putUuid("uuid", uuid);
        assertEquals(uuid, nbt.getUuid("uuid"));
    }

    @Test
    void testCompound() {
        var nbt = new CompoundTag();

        var compound = new CompoundTag(Map.of("test", new IntTag(32)));
        assertEquals(32, compound.getInt("test"));
        assertEquals(new CompoundTag(), nbt.getCompound("test"));
        nbt.put("test", compound);
        assertEquals(compound, nbt.getCompound("test"));
    }

    @Test
    void testList() {
        var nbt = new CompoundTag();

        assertEquals(new ListTag(), nbt.getList("list"));
        assertEquals(new ListTag(StringTag.class), nbt.getList("list", NBTConstants.TYPE_STRING));
        assertEquals(new ListTag(NBTConstants.TYPE_INT), nbt.getList("list", NBTConstants.TYPE_INT));
        final var list = new ListTag(StringTag.class);
        list.add(new StringTag("foo"));
        list.add(new StringTag("bar"));
        nbt.put("list", list);
        assertEquals(list, nbt.getList("list"));
        assertEquals(list, nbt.getList("list", NBTConstants.TYPE_STRING));
        assertEquals(new ListTag(IntTag.class), nbt.getList("list", NBTConstants.TYPE_INT));
    }

    private static void assertSimilar(double x, double y) {
        assertTrue(Math.abs(x - y) < 10e-12);
    }

    private static void assertSimilar(float x, float y) {
        assertTrue(Math.abs(x - y) < 10e-6);
    }
}