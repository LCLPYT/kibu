package work.lclpnet.kibu.scheduler;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class KibuScheduling implements ModInitializer {

    public static final String MOD_ID = "kibu-scheduler-api";
    private static final Logger logger = LoggerFactory.getLogger(MOD_ID);
    private static final RootScheduler rootScheduler = new RootScheduler(logger);

    @Override
    public void onInitialize() {
        ServerTickEvents.START_SERVER_TICK.register(server -> rootScheduler.tick());
    }

    @SuppressWarnings("unused")
    @Nonnull
    public static RootScheduler getRootScheduler() {
        return rootScheduler;
    }
}
