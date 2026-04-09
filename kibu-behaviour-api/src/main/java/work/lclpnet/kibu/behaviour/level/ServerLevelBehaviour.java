package work.lclpnet.kibu.behaviour.level;

import net.minecraft.server.level.ServerLevel;
import work.lclpnet.kibu.behaviour.type.KibuTickScheduler;

public class ServerLevelBehaviour {

    private ServerLevelBehaviour() {}

    public static void setFluidTicksEnabled(ServerLevel world, boolean enabled) {
        ((KibuTickScheduler) world.getFluidTicks()).kibu$setEnabled(enabled);
    }

    public static boolean isFluidTicksEnabled(ServerLevel world) {
        return ((KibuTickScheduler) world.getFluidTicks()).kibu$isEnabled();
    }
}
