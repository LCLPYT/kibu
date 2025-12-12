package work.lclpnet.kibu.hook.entity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface ItemFramePutItemCallback {

    Hook<ItemFramePutItemCallback> HOOK = HookFactory.createArrayBacked(ItemFramePutItemCallback.class,
            callbacks -> (itemFrame, stack, player, hand) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onPutIntoFrame(itemFrame, stack, player, hand)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    boolean onPutIntoFrame(ItemFrame itemFrame, ItemStack stack, Player player, InteractionHand hand);
}
