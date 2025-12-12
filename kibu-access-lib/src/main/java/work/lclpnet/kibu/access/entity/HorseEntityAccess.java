package work.lclpnet.kibu.access.entity;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Markings;
import net.minecraft.world.entity.animal.horse.Variant;
import net.minecraft.world.item.ItemStack;
import work.lclpnet.kibu.access.mixin.HorseEntityAccessor;

public class HorseEntityAccess {

    private HorseEntityAccess() {}

    public static void setVariant(Horse horse, Variant color, Markings marking) {
        ((HorseEntityAccessor) horse).invokeSetVariantAndMarkings(color, marking);
    }

    /**
     * Set the armor of a horse.
     * @param horse The horse entity.
     * @param armor The armor item stack.
     * @deprecated Use horse.equipBodyArmor(armor) instead
     */
    @Deprecated(forRemoval = true)
    public static void setArmor(Horse horse, ItemStack armor) {
        horse.setItemSlot(EquipmentSlot.BODY, armor);
    }
}
