package work.lclpnet.kibu.hook.entity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

    boolean onUseOnEntity(Player player, LivingEntity entity, InteractionHand hand, ItemStack stack);
}
