package work.lclpnet.kibu.jnbt.io;

import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.jnbt.NBTConstants;
import work.lclpnet.kibu.jnbt.TagInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class NbtIOHelperTest {

    @Test
    void testRead() throws IOException {
        var testNbt = Path.of("src/test/resources/test.nbt");

        TagInfo tagInfo;
        try (var in = Files.newInputStream(testNbt)) {
            tagInfo = NbtIOHelper.read(in);
        }

        assertEquals("", tagInfo.name());

        var tag = tagInfo.tag();
        assertNotNull(tag);
        assertEquals(NBTConstants.TYPE_COMPOUND, tag.getType());
        assertTrue(tag instanceof CompoundTag);

        var nbt = (CompoundTag) tag;
        assertEquals(5, nbt.keySet().size());

        var palette = nbt.getList("palette");
        assertEquals(3, palette.getValue().size());
    }

    @Test
    void testWrite() throws IOException {
        final var nbt = new CompoundTag();
        nbt.putString("value", "Hello World");

        final byte[] bytes;

        try (var out = new ByteArrayOutputStream()) {
            NbtIOHelper.write(nbt, out);
            bytes = out.toByteArray();
        }

        TagInfo tagInfo;
        try (var in = new ByteArrayInputStream(bytes)) {
            tagInfo = NbtIOHelper.read(in);
        }

        assertEquals("", tagInfo.name());

        assertEquals(nbt, tagInfo.tag());
    }

    @Test
    void testToArray() throws IOException {
        final var nbt = new CompoundTag();
        nbt.putString("value", "Hello World");

        final byte[] refBytes;

        try (var out = new ByteArrayOutputStream()) {
            NbtIOHelper.write(nbt, out);
            refBytes = out.toByteArray();
        }

        final byte[] bytes = NbtIOHelper.toArray(nbt);

        assertArrayEquals(refBytes, bytes);
    }

    @Test
    void testFromArray() throws IOException {
        var testNbt = Path.of("src/test/resources/test.nbt");

        TagInfo refTag;
        try (var in = Files.newInputStream(testNbt)) {
            refTag = NbtIOHelper.read(in);
        }

        final byte[] bytes = Files.readAllBytes(testNbt);

        TagInfo tag = NbtIOHelper.fromArray(bytes);

        assertEquals(refTag.name(), tag.name());
        assertEquals(refTag.rootTag(), tag.rootTag());
    }
}