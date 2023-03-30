package work.lclpnet.kibu.jnbt.io;

import work.lclpnet.kibu.jnbt.NBTInputStream;
import work.lclpnet.kibu.jnbt.NBTOutputStream;
import work.lclpnet.kibu.jnbt.Tag;
import work.lclpnet.kibu.jnbt.TagInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NbtIOHelper {

    private NbtIOHelper() {}

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
