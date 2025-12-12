package work.lclpnet.kibu.nbt;

import work.lclpnet.kibu.jnbt.*;
import work.lclpnet.kibu.nbt.mixin.NbtListAccessor;

import static work.lclpnet.kibu.jnbt.NBTConstants.*;

public class FabricNbtConversion {

    public static byte getTypeCode(Class<? extends net.minecraft.nbt.Tag> tagClass) {
        if (tagClass.equals(net.minecraft.nbt.ByteArrayTag.class)) {
            return TYPE_BYTE_ARRAY;
        } else if (tagClass.equals(net.minecraft.nbt.ByteTag.class)) {
            return TYPE_BYTE;
        } else if (tagClass.equals(net.minecraft.nbt.CompoundTag.class)) {
            return TYPE_COMPOUND;
        } else if (tagClass.equals(net.minecraft.nbt.DoubleTag.class)) {
            return TYPE_DOUBLE;
        } else if (tagClass.equals(net.minecraft.nbt.EndTag.class)) {
            return TYPE_END;
        } else if (tagClass.equals(net.minecraft.nbt.FloatTag.class)) {
            return TYPE_FLOAT;
        } else if (tagClass.equals(net.minecraft.nbt.IntArrayTag.class)) {
            return TYPE_INT_ARRAY;
        } else if (tagClass.equals(net.minecraft.nbt.IntTag.class)) {
            return TYPE_INT;
        } else if (tagClass.equals(net.minecraft.nbt.ListTag.class)) {
            return TYPE_LIST;
        } else if (tagClass.equals(net.minecraft.nbt.LongTag.class)) {
            return TYPE_LONG;
        } else if (tagClass.equals(net.minecraft.nbt.ShortTag.class)) {
            return TYPE_SHORT;
        } else if (tagClass.equals(net.minecraft.nbt.StringTag.class)) {
            return TYPE_STRING;
        } else if (tagClass.equals(net.minecraft.nbt.LongArrayTag.class)) {
            return TYPE_LONG_ARRAY;
        } else {
            throw new IllegalArgumentException("Unimplemented nbt class (%s).".formatted(tagClass.getSimpleName()));
        }
    }

    public static net.minecraft.nbt.Tag convert(Tag tag) {
        return switch (tag.getType()) {
            case TYPE_BYTE -> net.minecraft.nbt.ByteTag.valueOf(((ByteTag) tag).getValue());
            case TYPE_SHORT -> net.minecraft.nbt.ShortTag.valueOf(((ShortTag) tag).getValue());
            case TYPE_INT -> net.minecraft.nbt.IntTag.valueOf(((IntTag) tag).getValue());
            case TYPE_LONG -> net.minecraft.nbt.LongTag.valueOf(((LongTag) tag).getValue());
            case TYPE_FLOAT -> net.minecraft.nbt.FloatTag.valueOf(((FloatTag) tag).getValue());
            case TYPE_DOUBLE -> net.minecraft.nbt.DoubleTag.valueOf(((DoubleTag) tag).getValue());
            case TYPE_BYTE_ARRAY -> new net.minecraft.nbt.ByteArrayTag(((ByteArrayTag) tag).getValue());
            case TYPE_STRING -> net.minecraft.nbt.StringTag.valueOf(((StringTag) tag).getValue());
            case TYPE_LIST -> {
                ListTag nbt = (ListTag) tag;
                var list = new net.minecraft.nbt.ListTag();

                for (Tag element : nbt.getValue()) {
                    net.minecraft.nbt.Tag converted = convert(element);
                    if (converted instanceof net.minecraft.nbt.EndTag) continue;

                    list.add(converted);
                }

                yield list;
            }
            case TYPE_COMPOUND -> {
                CompoundTag nbt = (CompoundTag) tag;
                var compound = new net.minecraft.nbt.CompoundTag();

                for (String key : nbt.keySet()) {
                    Tag value = nbt.getNullable(key);
                    if (value == null) continue;

                    net.minecraft.nbt.Tag converted = convert(value);
                    if (converted instanceof net.minecraft.nbt.EndTag) continue;

                    compound.put(key, converted);
                }

                yield compound;
            }
            case TYPE_INT_ARRAY -> new net.minecraft.nbt.IntArrayTag(((IntArrayTag) tag).getValue());
            case TYPE_LONG_ARRAY -> new net.minecraft.nbt.LongArrayTag(((LongArrayTag) tag).getValue());
            default -> net.minecraft.nbt.EndTag.INSTANCE;
        };
    }

    public static Tag convert(net.minecraft.nbt.Tag tag) {
        return switch (tag.getId()) {
            case TYPE_BYTE -> new ByteTag(((net.minecraft.nbt.ByteTag) tag).byteValue());
            case TYPE_SHORT -> new ShortTag(((net.minecraft.nbt.ShortTag) tag).shortValue());
            case TYPE_INT -> new IntTag(((net.minecraft.nbt.IntTag) tag).intValue());
            case TYPE_LONG -> new LongTag(((net.minecraft.nbt.LongTag) tag).longValue());
            case TYPE_FLOAT -> new FloatTag(((net.minecraft.nbt.FloatTag) tag).floatValue());
            case TYPE_DOUBLE -> new DoubleTag(((net.minecraft.nbt.DoubleTag) tag).doubleValue());
            case TYPE_BYTE_ARRAY -> new ByteArrayTag(((net.minecraft.nbt.ByteArrayTag) tag).getAsByteArray());
            case TYPE_STRING -> new StringTag(tag.asString().orElse(""));
            case TYPE_LIST -> {
                net.minecraft.nbt.ListTag nbt = (net.minecraft.nbt.ListTag) tag;
                byte valueType = ((NbtListAccessor) (Object) nbt).invokeIdentifyRawElementType();
                var list = new ListTag(valueType);

                for (net.minecraft.nbt.Tag element : nbt) {
                    Tag converted = convert(element);
                    if (converted instanceof EndTag) continue;

                    list.add(converted);
                }

                yield list;
            }
            case TYPE_COMPOUND -> {
                net.minecraft.nbt.CompoundTag nbt = (net.minecraft.nbt.CompoundTag) tag;
                var compound = new CompoundTag();

                for (String key : nbt.keySet()) {
                    net.minecraft.nbt.Tag value = nbt.get(key);
                    if (value == null) continue;

                    Tag converted = convert(value);
                    if (converted instanceof EndTag) continue;

                    compound.put(key, converted);
                }

                yield compound;
            }
            case TYPE_INT_ARRAY -> new IntArrayTag(((net.minecraft.nbt.IntArrayTag) tag).getAsIntArray());
            case TYPE_LONG_ARRAY -> new LongArrayTag(((net.minecraft.nbt.LongArrayTag) tag).getAsLongArray());
            default -> EndTag.INSTANCE;
        };
    }

    @SuppressWarnings("unchecked")
    public static <T extends net.minecraft.nbt.Tag> T convert(Tag tag, Class<T> fabricClass) {
        byte type = getTypeCode(fabricClass);

        if ((int) type != tag.getType()) {
            throw new IllegalStateException("Tag is not convertible to %s".formatted(fabricClass.getSimpleName()));
        }

        return (T) convert(tag);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Tag> T convert(net.minecraft.nbt.Tag tag, Class<T> kibuClass) {
        int type = NBTUtils.getTypeCode(kibuClass);

        if (type != (int) tag.getId()) {
            throw new IllegalStateException("Tag is not convertible to %s".formatted(kibuClass.getSimpleName()));
        }

        return (T) convert(tag);
    }
}
