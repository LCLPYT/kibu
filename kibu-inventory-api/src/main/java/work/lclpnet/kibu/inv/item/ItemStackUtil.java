package work.lclpnet.kibu.inv.item;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

import java.util.List;

public class ItemStackUtil {

    private static final Style LORE_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withItalic(false);

    public static void setLore(ItemStack stack, List<Component> lore) {
        List<Component> transformed = Lists.transform(lore, t -> ComponentUtils.mergeStyles(t.copy(), LORE_STYLE));
        stack.set(DataComponents.LORE, new ItemLore(transformed));
    }
}
