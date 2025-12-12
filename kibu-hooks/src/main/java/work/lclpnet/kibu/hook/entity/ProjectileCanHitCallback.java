package work.lclpnet.kibu.hook.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface ProjectileCanHitCallback {

    Hook<ProjectileCanHitCallback> HOOK = HookFactory.createArrayBacked(ProjectileCanHitCallback.class, callbacks -> (projectile, entity) -> {
        for (var cb : callbacks) {
            if (!cb.canHit(projectile, entity)) {
                return false;
            }
        }

        return true;
    });

    boolean canHit(Projectile projectile, Entity entity);
}
