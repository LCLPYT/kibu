package work.lclpnet.kibu.access.entity;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.HorseColor;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.HorseMarking;
import net.minecraft.item.ItemStack;
import work.lclpnet.kibu.access.mixin.HorseEntityAccessor;

public class HorseEntityAccess {

    private HorseEntityAccess() {}

    public static void setVariant(HorseEntity horse, HorseColor color, HorseMarking marking) {
        ((HorseEntityAccessor) horse).invokeSetHorseVariant(color, marking);
    }

    /**
     * Set the armor of a horse.
     * @param horse The horse entity.
     * @param armor The armor item stack.
     * @deprecated Use horse.equipBodyArmor(armor) instead
     */
    @Deprecated(forRemoval = true)
    public static void setArmor(HorseEntity horse, ItemStack armor) {
        horse.equipStack(EquipmentSlot.BODY, armor);
    }
}
