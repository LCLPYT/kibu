package work.lclpnet.kibu.hook.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface PressurePlateCallback {

    Hook<PressurePlateCallback> HOOK = HookFactory.createArrayBacked(PressurePlateCallback.class, callbacks -> (world, pos, entity) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onPress(world, pos, entity))
                cancelled = true;

        return cancelled;
    });

    boolean onPress(Level world, BlockPos pos, Entity entity);
}
