package work.lclpnet.kibu.access.entity;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class LlamaEntityAccess {

    private LlamaEntityAccess() {}

    public static void setCarpetColor(Llama llama, @Nullable DyeColor color) {
        if (color == null) {
            llama.setItemSlot(EquipmentSlot.BODY, ItemStack.EMPTY);
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

        llama.setItemSlot(EquipmentSlot.BODY, new ItemStack(item));
    }
}
