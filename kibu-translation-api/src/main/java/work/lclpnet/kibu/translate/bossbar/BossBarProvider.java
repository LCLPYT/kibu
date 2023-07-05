package work.lclpnet.kibu.translate.bossbar;

import net.minecraft.entity.boss.CommandBossBar;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public interface BossBarProvider {

    CommandBossBar createBossBar(Identifier id, Text text);
}
