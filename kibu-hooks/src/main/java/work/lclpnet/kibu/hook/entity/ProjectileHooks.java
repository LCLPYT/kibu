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

    public interface HitBlock {
        void onHitBlock(ProjectileEntity projectile, BlockHitResult hit);
    }
}
