package work.lclpnet.kibu.hook.entity;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface EntityBossBarCallback {

    Hook<EntityBossBarCallback> HOOK = HookFactory.createArrayBacked(EntityBossBarCallback.class, callbacks -> (entity, bossBar, player) -> {
        boolean cancel = false;

        for (var cb : callbacks) {
            if (cb.onShow(entity, bossBar, player)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onShow(Entity entity, ServerBossEvent bossBar, ServerPlayer player);
}
