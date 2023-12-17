package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface ItemFrameRotateCallback {

    Hook<ItemFrameRotateCallback> HOOK = HookFactory.createArrayBacked(ItemFrameRotateCallback.class,
            callbacks -> (itemFrame, player, hand) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onRotateFrame(itemFrame, player, hand)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    boolean onRotateFrame(ItemFrameEntity itemFrame, PlayerEntity player, Hand hand);
}
