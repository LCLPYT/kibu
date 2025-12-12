package work.lclpnet.kibu.hook.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface ItemFrameRemoveItemCallback {

    Hook<ItemFrameRemoveItemCallback> HOOK = HookFactory.createArrayBacked(ItemFrameRemoveItemCallback.class,
            callbacks -> (itemFrame, attacker) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onRemoveItem(itemFrame, attacker)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    boolean onRemoveItem(ItemFrame itemFrame, @Nullable Entity attacker);
}
