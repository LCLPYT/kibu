package work.lclpnet.kibu.hook.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface PlayerSpawnPointChangeCallback {

    Hook<PlayerSpawnPointChangeCallback> HOOK = HookFactory.createArrayBacked(PlayerSpawnPointChangeCallback.class, callbacks -> (player, world, pos) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onChange(player, world, pos))
                cancelled = true;

        return cancelled;
    });

    boolean onChange(PlayerEntity player, World world, BlockPos pos);
}
