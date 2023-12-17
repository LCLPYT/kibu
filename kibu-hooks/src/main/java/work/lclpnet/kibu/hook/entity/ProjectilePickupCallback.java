package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface ProjectilePickupCallback {

    Hook<ProjectilePickupCallback> HOOK = HookFactory.createArrayBacked(ProjectilePickupCallback.class,
            callbacks -> (player, projectile) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onPickup(player, projectile)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    boolean onPickup(PlayerEntity player, PersistentProjectileEntity projectile);
}
