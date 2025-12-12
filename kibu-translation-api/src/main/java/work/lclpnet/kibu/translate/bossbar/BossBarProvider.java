package work.lclpnet.kibu.translate.bossbar;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.bossevents.CustomBossEvent;

public interface BossBarProvider {

    CustomBossEvent createBossBar(Identifier id, Component text);
}
