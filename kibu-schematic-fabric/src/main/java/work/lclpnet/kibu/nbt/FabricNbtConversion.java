package work.lclpnet.kibu.nbt;

import net.minecraft.nbt.*;
import work.lclpnet.kibu.jnbt.*;

import static work.lclpnet.kibu.jnbt.NBTConstants.*;

public class FabricNbtConversion {

    public static byte getTypeCode(Class<? extends NbtElement> tagClass) {
        if (tagClass.equals(NbtByteArray.class)) {
            return TYPE_BYTE_ARRAY;
        } else if (tagClass.equals(NbtByte.class)) {
            return TYPE_BYTE;
        } else if (tagClass.equals(NbtCompound.class)) {
            return TYPE_COMPOUND;
        } else if (tagClass.equals(NbtDouble.class)) {
            return TYPE_DOUBLE;
        } else if (tagClass.equals(NbtEnd.class)) {
            return TYPE_END;
        } else if (tagClass.equals(NbtFloat.class)) {
            return TYPE_FLOAT;
        } else if (tagClass.equals(NbtIntArray.class)) {
            return TYPE_INT_ARRAY;
        } else if (tagClass.equals(NbtInt.class)) {
            return TYPE_INT;
        } else if (tagClass.equals(NbtList.class)) {
            return TYPE_LIST;
        } else if (tagClass.equals(NbtLong.class)) {
            return TYPE_LONG;
        } else if (tagClass.equals(NbtShort.class)) {
            return TYPE_SHORT;
        } else if (tagClass.equals(NbtString.class)) {
            return TYPE_STRING;
        } else if (tagClass.equals(NbtLongArray.class)) {
            return TYPE_LONG_ARRAY;
        } else {
            throw new IllegalArgumentException("Unimplemented nbt class (%s).".formatted(tagClass.getSimpleName()));
        }
    }

    public static NbtElement convert(Tag tag) {
        return switch (tag.getType()) {
            case TYPE_BYTE -> NbtByte.of(((ByteTag) tag).getValue());
            case TYPE_SHORT -> NbtShort.of(((ShortTag) tag).getValue());
            case TYPE_INT -> NbtInt.of(((IntTag) tag).getValue());
            case TYPE_LONG -> NbtLong.of(((LongTag) tag).getValue());
            case TYPE_FLOAT -> NbtFloat.of(((FloatTag) tag).getValue());
            case TYPE_DOUBLE -> NbtDouble.of(((DoubleTag) tag).getValue());
            case TYPE_BYTE_ARRAY -> new NbtByteArray(((ByteArrayTag) tag).getValue());
            case TYPE_STRING -> NbtString.of(((StringTag) tag).getValue());
            case TYPE_LIST -> {
                ListTag nbt = (ListTag) tag;
                var list = new NbtList();

                for (Tag element : nbt.getValue()) {
                    NbtElement converted = convert(element);
                    if (converted instanceof NbtEnd) continue;

                    list.add(converted);
                }

                yield list;
            }
            case TYPE_COMPOUND -> {
                CompoundTag nbt = (CompoundTag) tag;
                var compound = new NbtCompound();

                for (String key : nbt.keySet()) {
                    Tag value = nbt.getNullable(key);
                    if (value == null) continue;

                    NbtElement converted = convert(value);
                    if (converted instanceof NbtEnd) continue;

                    compound.put(key, converted);
                }

                yield compound;
            }
            case TYPE_INT_ARRAY -> new NbtIntArray(((IntArrayTag) tag).getValue());
            case TYPE_LONG_ARRAY -> new NbtLongArray(((LongArrayTag) tag).getValue());
            default -> NbtEnd.INSTANCE;
        };
    }

    public static Tag convert(NbtElement tag) {
        return switch (tag.getType()) {
            case TYPE_BYTE -> new ByteTag(((NbtByte) tag).byteValue());
            case TYPE_SHORT -> new ShortTag(((NbtShort) tag).shortValue());
            case TYPE_INT -> new IntTag(((NbtInt) tag).intValue());
            case TYPE_LONG -> new LongTag(((NbtLong) tag).longValue());
            case TYPE_FLOAT -> new FloatTag(((NbtFloat) tag).floatValue());
            case TYPE_DOUBLE -> new DoubleTag(((NbtDouble) tag).doubleValue());
            case TYPE_BYTE_ARRAY -> new ByteArrayTag(((NbtByteArray) tag).getByteArray());
            case TYPE_STRING -> new StringTag(tag.asString());
            case TYPE_LIST -> {
                NbtList nbt = (NbtList) tag;
                var list = new ListTag(nbt.getHeldType());

                for (NbtElement element : nbt) {
                    Tag converted = convert(element);
                    if (converted instanceof EndTag) continue;

                    list.add(converted);
                }

                yield list;
            }
            case TYPE_COMPOUND -> {
                NbtCompound nbt = (NbtCompound) tag;
                var compound = new CompoundTag();

                for (String key : nbt.getKeys()) {
                    NbtElement value = nbt.get(key);
                    if (value == null) continue;

                    Tag converted = convert(value);
                    if (converted instanceof EndTag) continue;

                    compound.put(key, converted);
                }

                yield compound;
            }
            case TYPE_INT_ARRAY -> new IntArrayTag(((NbtIntArray) tag).getIntArray());
            case TYPE_LONG_ARRAY -> new LongArrayTag(((NbtLongArray) tag).getLongArray());
            default -> EndTag.INSTANCE;
        };
    }

    @SuppressWarnings("unchecked")
    public static <T extends NbtElement> T convert(Tag tag, Class<T> fabricClass) {
        byte type = getTypeCode(fabricClass);

        if ((int) type != tag.getType()) {
            throw new IllegalStateException("Tag is not convertible to %s".formatted(fabricClass.getSimpleName()));
        }

        return (T) convert(tag);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Tag> T convert(NbtElement tag, Class<T> kibuClass) {
        int type = NBTUtils.getTypeCode(kibuClass);

        if (type != (int) tag.getType()) {
            throw new IllegalStateException("Tag is not convertible to %s".formatted(kibuClass.getSimpleName()));
        }

        return (T) convert(tag);
    }
}
