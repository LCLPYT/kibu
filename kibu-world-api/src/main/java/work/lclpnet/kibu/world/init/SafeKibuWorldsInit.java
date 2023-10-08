package work.lclpnet.kibu.world.init;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import work.lclpnet.kibu.world.KibuWorlds;
import work.lclpnet.kibu.world.WorldHandleTracker;
import work.lclpnet.kibu.world.WorldManager;
import xyz.nucleoid.fantasy.RuntimeWorld;

public class SafeKibuWorldsInit {

    /**
     * Called when dynamic dependencies are verified.
     */
    public void initSafe() {
        ServerWorldEvents.UNLOAD.register((server, world) -> {
            if (!(world instanceof RuntimeWorld runtimeWorld)) return;

            WorldManager worldManager = KibuWorlds.getInstance().getWorldManager(server);

            if (worldManager instanceof WorldHandleTracker tracker) {
                tracker.unregisterWorld(runtimeWorld);
            }
        });
    }
}
