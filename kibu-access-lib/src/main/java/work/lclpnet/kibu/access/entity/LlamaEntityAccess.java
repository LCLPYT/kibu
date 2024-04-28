package work.lclpnet.kibu.access.entity;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.Nullable;

public class LlamaEntityAccess {

    private LlamaEntityAccess() {}

    public static void setCarpetColor(LlamaEntity llama, @Nullable DyeColor color) {
        if (color == null) {
            llama.equipStack(EquipmentSlot.BODY, ItemStack.EMPTY);
            return;
        }

        Item item = switch (color) {
            case WHITE -> Items.WHITE_CARPET;
            case ORANGE -> Items.ORANGE_CARPET;
            case MAGENTA -> Items.MAGENTA_CARPET;
            case LIGHT_BLUE -> Items.LIGHT_BLUE_CARPET;
            case YELLOW -> Items.YELLOW_CARPET;
            case LIME -> Items.LIME_CARPET;
            case PINK -> Items.PINK_CARPET;
            case GRAY -> Items.GRAY_CARPET;
            case LIGHT_GRAY -> Items.LIGHT_GRAY_CARPET;
            case CYAN -> Items.CYAN_CARPET;
            case PURPLE -> Items.PURPLE_CARPET;
            case BLUE -> Items.BLUE_CARPET;
            case BROWN -> Items.BROWN_CARPET;
            case GREEN -> Items.GREEN_CARPET;
            case RED -> Items.RED_CARPET;
            case BLACK -> Items.BLACK_CARPET;
        };

        llama.equipStack(EquipmentSlot.BODY, new ItemStack(item));
    }
}
