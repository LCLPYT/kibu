package work.lclpnet.kibu.world;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import xyz.nucleoid.fantasy.RuntimeWorld;

public class KibuWorldsInit implements ModInitializer {

    @Override
    public void onInitialize() {
        ServerWorldEvents.UNLOAD.register((server, world) -> {
            if (!(world instanceof RuntimeWorld runtimeWorld)) return;

            KibuWorldsImpl.getInstance().unregisterWorld(runtimeWorld);
        });
    }
}
