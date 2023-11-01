package work.lclpnet.kibu.hook.world;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface BlockBreakParticleCallback {

    Hook<BlockBreakParticleCallback> HOOK = HookFactory.createArrayBacked(BlockBreakParticleCallback.class, callbacks -> (world, pos, state) -> {
        boolean cancelled = false;

        for (var callback : callbacks) {
            if (callback.onSpawnParticles(world, pos, state)) {
                cancelled = true;
            }
        }

        return cancelled;
    });

    boolean onSpawnParticles(World world, BlockPos pos, BlockState state);
}
