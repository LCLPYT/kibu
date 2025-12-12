package work.lclpnet.kibu.hook.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
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

    boolean onSpawnParticles(Level world, BlockPos pos, BlockState state);
}
