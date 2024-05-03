package work.lclpnet.kibu.inv.item;

import com.google.common.collect.Lists;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;

import java.util.List;

public class ItemStackUtil {

    private static final Style LORE_STYLE = Style.EMPTY.withColor(Formatting.DARK_PURPLE).withItalic(false);

    public static void setLore(ItemStack stack, List<Text> lore) {
        List<Text> transformed = Lists.transform(lore, t -> Texts.setStyleIfAbsent(t.copy(), LORE_STYLE));
        stack.set(DataComponentTypes.LORE, new LoreComponent(transformed));
    }
}
