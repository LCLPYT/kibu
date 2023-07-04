package work.lclpnet.kibu.hook.world;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

    boolean onPress(World world, BlockPos pos, Entity entity);
}
