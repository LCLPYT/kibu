package work.lclpnet.kibu.behaviour.world;

import net.minecraft.server.world.ServerWorld;
import work.lclpnet.kibu.behaviour.type.KibuTickScheduler;

public class ServerWorldBehaviour {

    private ServerWorldBehaviour() {}

    public static void setFluidTicksEnabled(ServerWorld world, boolean enabled) {
        ((KibuTickScheduler) world.getFluidTickScheduler()).kibu$setEnabled(enabled);
    }

    public static boolean isFluidTicksEnabled(ServerWorld world) {
        return ((KibuTickScheduler) world.getFluidTickScheduler()).kibu$isEnabled();
    }
}
