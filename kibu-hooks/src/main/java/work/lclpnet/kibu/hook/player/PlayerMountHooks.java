package work.lclpnet.kibu.hook.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
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
        void doAfter(ServerPlayer player, Entity vehicle);
    }
}
