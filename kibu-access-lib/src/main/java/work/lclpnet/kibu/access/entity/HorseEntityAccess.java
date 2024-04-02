package work.lclpnet.kibu.access.entity;

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

    public static void setArmor(HorseEntity horse, ItemStack armor) {
        ((HorseEntityAccessor) horse).invokeSetArmorTypeFromStack(armor);
    }
}
