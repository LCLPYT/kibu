package work.lclpnet.kibu.hook.entity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

    boolean onManipulate(ArmorStand armorStand, Player player, EquipmentSlot slot, ItemStack stack, InteractionHand hand);
}
