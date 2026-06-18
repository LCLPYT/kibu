package work.lclpnet.kibu.access.entity;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.equine.Llama;
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

        Item item = Items.CARPET.pick(color);

        llama.setItemSlot(EquipmentSlot.BODY, new ItemStack(item));
    }
}
