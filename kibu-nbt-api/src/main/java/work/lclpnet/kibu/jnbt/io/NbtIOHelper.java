package work.lclpnet.kibu.jnbt.io;

import work.lclpnet.kibu.jnbt.NBTInputStream;
import work.lclpnet.kibu.jnbt.NBTOutputStream;
import work.lclpnet.kibu.jnbt.Tag;
import work.lclpnet.kibu.jnbt.TagInfo;

import java.io.*;

public class NbtIOHelper {

    private NbtIOHelper() {}

    public static byte[] toArray(Tag tag) throws IOException {
        var out = new ByteArrayOutputStream();
        write(tag, out);
        return out.toByteArray();
    }

    public static byte[] toArrayUncompressed(Tag tag) throws IOException {
        var out = new ByteArrayOutputStream();
        writeUncompressed(tag, out);
        return out.toByteArray();
    }

    public static byte[] toArray(Tag tag, String name) throws IOException {
        var out = new ByteArrayOutputStream();
        write(tag, name, out);
        return out.toByteArray();
    }

    public static byte[] toArrayUncompressed(Tag tag, String name) throws IOException {
        var out = new ByteArrayOutputStream();
        writeUncompressed(tag, name, out);
        return out.toByteArray();
    }

    public static void write(Tag tag, OutputStream out) throws IOException {
        write(tag, "", out);
    }

    public static void writeUncompressed(Tag tag, OutputStream out) throws IOException {
        writeUncompressed(tag, "", out);
    }

    public static void write(Tag tag, String name, OutputStream out) throws IOException {
        try (var nbtOut = new NBTOutputStream(out)) {
            nbtOut.writeTag(name, tag);
        }
    }

    public static void writeUncompressed(Tag tag, String name, OutputStream out) throws IOException {
        try (var nbtOut = new NBTOutputStream(out, false)) {
            nbtOut.writeTag(name, tag);
        }
    }

    public static TagInfo fromArray(byte[] array) throws IOException {
        var in = new ByteArrayInputStream(array);
        return read(in);
    }

    public static TagInfo fromArrayUncompressed(byte[] array) throws IOException {
        var in = new ByteArrayInputStream(array);
        return readUncompressed(in);
    }

    public static TagInfo read(InputStream in) throws IOException {
        try (var nbtIn = new NBTInputStream(in)) {
            return nbtIn.readTag();
        }
    }

    public static TagInfo readUncompressed(InputStream in) throws IOException {
        try (var nbtIn = new NBTInputStream(in, false)) {
            return nbtIn.readTag();
        }
    }
}
