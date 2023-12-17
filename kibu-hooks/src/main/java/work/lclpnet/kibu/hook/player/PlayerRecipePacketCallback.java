package work.lclpnet.kibu.hook.player;

import net.minecraft.network.packet.s2c.play.UnlockRecipesS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface PlayerRecipePacketCallback {

    Hook<PlayerRecipePacketCallback> HOOK = HookFactory.createArrayBacked(PlayerRecipePacketCallback.class,
            callbacks -> (player, packet) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onRecipeUpdate(player, packet)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    boolean onRecipeUpdate(ServerPlayerEntity player, UnlockRecipesS2CPacket packet);
}
