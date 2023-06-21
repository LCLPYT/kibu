package work.lclpnet.kibu.hook.player;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public class PlayerMountHooks {

    private PlayerMountHooks() {}

    public static final Hook<AfterMount> MOUNTED = HookFactory.createArrayBacked(AfterMount.class, callbacks -> (player, vehicle) -> {
        for (var callback : callbacks) {
            callback.doAfter(player, vehicle);
        }
    });

    public static final Hook<AfterMount> DISMOUNTED = HookFactory.createArrayBacked(AfterMount.class, callbacks -> (player, vehicle) -> {
        for (var callback : callbacks) {
            callback.doAfter(player, vehicle);
        }
    });

    public interface AfterMount {
        void doAfter(ServerPlayerEntity player, Entity vehicle);
    }
}
