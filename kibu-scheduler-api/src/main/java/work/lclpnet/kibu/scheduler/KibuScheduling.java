package work.lclpnet.kibu.scheduler;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.lclpnet.kibu.scheduler.api.Scheduler;

import javax.annotation.Nonnull;

public class KibuScheduling implements ModInitializer {

    public static final String MOD_ID = "kibu-scheduler-api";
    private static final Logger logger = LoggerFactory.getLogger(MOD_ID);
    private static final Scheduler globalScheduler = new Scheduler(logger);

    @Override
    public void onInitialize() {
        ServerTickEvents.START_SERVER_TICK.register(server -> globalScheduler.tick());
    }

    @SuppressWarnings("unused")
    @Nonnull
    public static Scheduler getScheduler() {
        return globalScheduler;
    }
}
