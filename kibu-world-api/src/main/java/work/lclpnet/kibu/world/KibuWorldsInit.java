package work.lclpnet.kibu.world;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nucleoid.fantasy.RuntimeWorld;

public class KibuWorldsInit implements ModInitializer {

    private static final String FANTASY_MOD_ID = "fantasy";
    private static final String KIBU_WORLD_API_MOD_ID = "kibu-world-api";
    private static final Logger LOGGER = LoggerFactory.getLogger(FANTASY_MOD_ID);
    private static boolean funtional = false;

    @Override
    public void onInitialize() {
        if (!isFantasyPresent()) {
            LOGGER.warn("Mod '{}' is not loaded. Disabling {}...", FANTASY_MOD_ID, KIBU_WORLD_API_MOD_ID);
            return;
        }

        new SafeKibuWorldsInit().initSafe();

        funtional = true;
    }

    private static boolean isFantasyPresent() {
        return FabricLoader.getInstance().isModLoaded(FANTASY_MOD_ID);
    }

    /**
     * Check whether kibu-world-api is functional.
     * The mod might not be functional, if:
     * <ul>
     *     <li>it wasn't loaded yet</li>
     *     <li>dynamic dependencies, such as the 'fantasy' mod are not loaded</li>
     * </ul>
     * @return Whether the mod is functional.
     */
    public static boolean isFunctional() {
        return funtional;
    }
}
