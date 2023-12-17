package work.lclpnet.kibu.hook.player;

import net.minecraft.network.packet.s2c.play.AdvancementUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface PlayerAdvancementPacketCallback {

    Hook<PlayerAdvancementPacketCallback> HOOK = HookFactory.createArrayBacked(PlayerAdvancementPacketCallback.class,
            callbacks -> (player, packet) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onAdvancementUpdate(player, packet)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    boolean onAdvancementUpdate(ServerPlayerEntity player, AdvancementUpdateS2CPacket packet);
}
