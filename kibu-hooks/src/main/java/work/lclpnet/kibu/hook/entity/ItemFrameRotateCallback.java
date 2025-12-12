package work.lclpnet.kibu.hook.entity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
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

    boolean onRotateFrame(ItemFrame itemFrame, Player player, InteractionHand hand);
}
