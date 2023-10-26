package work.lclpnet.kibu.hook.player;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface PlayerDeathCallback {

    Hook<PlayerDeathCallback> HOOK = HookFactory.createArrayBacked(PlayerDeathCallback.class, callbacks ->
            (player, source) -> {
                for (var callback : callbacks) {
                    callback.onDeath(player, source);
                }
            });

    void onDeath(ServerPlayerEntity player, DamageSource source);
}
