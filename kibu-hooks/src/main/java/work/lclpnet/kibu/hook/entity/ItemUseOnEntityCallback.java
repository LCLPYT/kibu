package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface ItemUseOnEntityCallback {

    Hook<ItemUseOnEntityCallback> HOOK = HookFactory.createArrayBacked(ItemUseOnEntityCallback.class,
            callbacks -> (player, entity, hand, stack) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onUseOnEntity(player, entity, hand, stack)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    boolean onUseOnEntity(PlayerEntity player, LivingEntity entity, Hand hand, ItemStack stack);
}
