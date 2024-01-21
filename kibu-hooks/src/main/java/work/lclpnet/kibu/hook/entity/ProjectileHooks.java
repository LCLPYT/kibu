package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public class ProjectileHooks {

    private ProjectileHooks() {}

    public static final Hook<HitBlock> HIT_BLOCK = HookFactory.createArrayBacked(HitBlock.class, (hooks) -> (projectile, hit) -> {
        for (var hook : hooks) {
            hook.onHitBlock(projectile, hit);
        }
    });

    public static final Hook<AffectBlock> BREAK_DECORATED_POT = HookFactory.createArrayBacked(AffectBlock.class,
            hooks -> (projectile, hit) -> {
                boolean cancel = false;

                for (var hook : hooks) {
                    if (hook.onAffect(projectile, hit)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    public interface HitBlock {
        void onHitBlock(ProjectileEntity projectile, BlockHitResult hit);
    }

    public interface AffectBlock {
        boolean onAffect(ProjectileEntity projectile, BlockHitResult hit);
    }
}
