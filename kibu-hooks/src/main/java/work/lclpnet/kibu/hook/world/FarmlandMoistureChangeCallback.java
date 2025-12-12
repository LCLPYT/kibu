package work.lclpnet.kibu.hook.world;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

/**
 * Called when the moisture of a farmland block changes.
 */
public interface FarmlandMoistureChangeCallback {

    Hook<FarmlandMoistureChangeCallback> HOOK = HookFactory.createArrayBacked(FarmlandMoistureChangeCallback.class, hooks -> (world, pos, moisture) -> {
        boolean cancel = false;

        for (var hook : hooks) {
            if (hook.onMoistureChange(world, pos, moisture)) {
                cancel = true;
            }
        }

        return cancel;
    });

    /**
     * Called when the moisture of a farmland block changes.
     * @param world The world.
     * @param pos The block position of the farmland
     * @param moisture The moisture level after the change is complete. Will be -1 when the farmland would be converted to dirt.
     * @return True, if the change should be cancelled. False for default behaviour.
     */
    boolean onMoistureChange(ServerLevel world, BlockPos pos, int moisture);
}
