package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface LeashAttachCallback {

    Hook<LeashAttachCallback> HOOK = HookFactory.createArrayBacked(LeashAttachCallback.class,
            callbacks -> (player, world, pos) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onAttach(player, world, pos)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    boolean onAttach(PlayerEntity player, World world, BlockPos pos);
}
