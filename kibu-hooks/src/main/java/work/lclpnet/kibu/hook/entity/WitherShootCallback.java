package work.lclpnet.kibu.hook.entity;

import net.minecraft.world.entity.boss.wither.WitherBoss;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface WitherShootCallback {

    Hook<WitherShootCallback> HOOK = HookFactory.createArrayBacked(WitherShootCallback.class, callbacks -> (wither, targetX, targetY, targetZ) -> {
        boolean cancel = false;

        for (var cb : callbacks) {
            if (cb.onShootAt(wither, targetX, targetY, targetZ)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onShootAt(WitherBoss wither, double targetX, double targetY, double targetZ);
}
