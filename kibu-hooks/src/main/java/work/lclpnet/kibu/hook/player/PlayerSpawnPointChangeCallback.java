package work.lclpnet.kibu.hook.player;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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

    boolean onChange(Player player, Level world, BlockPos pos);
}
