package work.lclpnet.kibu.translate.util;

import net.minecraft.server.bossevents.CustomBossEvent;
import work.lclpnet.kibu.translate.type.TransientCommandBossBar;

public class TransientBossBars {

    public static void setTransient(CustomBossEvent bossBar, boolean isTransient) {
        ((TransientCommandBossBar) bossBar).kibu$setTransient(isTransient);
    }

    public static boolean isTransient(CustomBossEvent bossBar) {
        return ((TransientCommandBossBar) bossBar).kibu$isTransient();
    }
}
