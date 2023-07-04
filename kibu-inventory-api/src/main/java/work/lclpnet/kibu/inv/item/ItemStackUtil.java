package work.lclpnet.kibu.inv.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class ItemStackUtil {

    private static final Style LORE_STYLE = Style.EMPTY.withColor(Formatting.DARK_PURPLE).withItalic(false);

    /**
     * Gets the lore of a {@link ItemStack}
     * @param stack The item stack
     * @return A mutable copy of the {@link ItemStack}s lore.
     */
    public static List<Text> getLore(ItemStack stack) {
        NbtCompound display = stack.getSubNbt(ItemStack.DISPLAY_KEY);

        if (display == null) {
            return new ArrayList<>();
        }

        if (display.getType(ItemStack.LORE_KEY) != NbtElement.LIST_TYPE) {
            return new ArrayList<>();
        }

        final List<Text> lore = new ArrayList<>();
        final NbtList nbtList = display.getList(ItemStack.LORE_KEY, NbtElement.STRING_TYPE);

        for (int i = 0; i < nbtList.size(); i++) {
            String json = nbtList.getString(i);

            try {
                MutableText text = Text.Serializer.fromJson(json);
                if (text == null) continue;

                lore.add(text);
            } catch (Exception ignored) {}
        }

        return lore;
    }

    public static void setLore(ItemStack stack, List<Text> lore) {
        NbtCompound display = stack.getOrCreateSubNbt(ItemStack.DISPLAY_KEY);
        NbtList list = new NbtList();

        for (Text text : lore) {
            String json = Text.Serializer.toJson(text.copy().fillStyle(LORE_STYLE));
            list.add(NbtString.of(json));
        }

        display.put(ItemStack.LORE_KEY, list);
    }

    public static boolean isUnbreakable(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return nbt != null && nbt.getBoolean("Unbreakable");
    }

    public static void setUnbreakable(ItemStack stack, boolean unbreakable) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.put("Unbreakable", NbtByte.of(unbreakable));
    }

    public static OptionalInt getCustomModelData(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();

        if (nbt == null || nbt.getType("CustomModelData") != NbtElement.INT_TYPE) {
            return OptionalInt.empty();
        }

        return OptionalInt.of(nbt.getInt("CustomModelData"));
    }

    public static void setCustomModelData(ItemStack stack, int customModelData) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.put("CustomModelData", NbtInt.of(customModelData));
    }
}
