package work.lclpnet.kibu.world;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import xyz.nucleoid.fantasy.RuntimeWorld;

public class SafeKibuWorldsInit {

    /**
     * Called when dynamic dependencies are verified.
     */
    public void initSafe() {
        ServerWorldEvents.UNLOAD.register((server, world) -> {
            if (!(world instanceof RuntimeWorld runtimeWorld)) return;

            KibuWorldsImpl.getInstance().unregisterWorld(runtimeWorld);
        });
    }
}
