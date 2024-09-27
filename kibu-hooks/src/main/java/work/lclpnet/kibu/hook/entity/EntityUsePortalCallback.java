package work.lclpnet.kibu.hook.entity;

import net.minecraft.block.Portal;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface EntityUsePortalCallback {

    Hook<EntityUsePortalCallback> HOOK = HookFactory.createArrayBacked(EntityUsePortalCallback.class, callbacks -> (entity, portal, pos) -> {
        boolean cancel = false;

        for (EntityUsePortalCallback callback : callbacks) {
            if (callback.onUsePortal(entity, portal, pos)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onUsePortal(Entity entity, Portal portal, BlockPos pos);
}
