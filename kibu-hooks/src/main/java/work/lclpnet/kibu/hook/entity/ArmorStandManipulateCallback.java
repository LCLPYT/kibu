package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface ArmorStandManipulateCallback {

    Hook<ArmorStandManipulateCallback> HOOK = HookFactory.createArrayBacked(ArmorStandManipulateCallback.class,
            callbacks -> (armorStand, player, slot, stack, hand) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onManipulate(armorStand, player, slot, stack, hand)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    boolean onManipulate(ArmorStandEntity armorStand, PlayerEntity player, EquipmentSlot slot, ItemStack stack, Hand hand);
}
