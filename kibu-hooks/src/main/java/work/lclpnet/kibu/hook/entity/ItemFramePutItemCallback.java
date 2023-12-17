package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
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

    boolean onPutIntoFrame(ItemFrameEntity itemFrame, ItemStack stack, PlayerEntity player, Hand hand);
}
