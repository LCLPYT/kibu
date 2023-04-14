package work.lclpnet.kibu.hook.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface PlayerSpawnPointChangeHook {

    Hook<PlayerSpawnPointChangeHook> HOOK = HookFactory.createArrayBacked(PlayerSpawnPointChangeHook.class, callbacks -> (player, world, pos) -> {
        for (var callback : callbacks)
            if (callback.onChange(player, world, pos))
                return true;

        return false;
    });

    boolean onChange(PlayerEntity player, World world, BlockPos pos);
}
