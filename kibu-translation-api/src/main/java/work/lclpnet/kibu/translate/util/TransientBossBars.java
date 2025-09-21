package work.lclpnet.kibu.translate.util;

import net.minecraft.entity.boss.CommandBossBar;
import work.lclpnet.kibu.translate.type.TransientCommandBossBar;

public class TransientBossBars {

    public static void setTransient(CommandBossBar bossBar, boolean isTransient) {
        ((TransientCommandBossBar) bossBar).kibu$setTransient(isTransient);
    }

    public static boolean isTransient(CommandBossBar bossBar) {
        return ((TransientCommandBossBar) bossBar).kibu$isTransient();
    }
}
