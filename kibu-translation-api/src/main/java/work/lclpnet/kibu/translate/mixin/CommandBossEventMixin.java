package work.lclpnet.kibu.translate.mixin;

import net.minecraft.server.bossevents.CustomBossEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import work.lclpnet.kibu.translate.type.TransientCommandBossBar;

@Mixin(CustomBossEvent.class)
public class CommandBossEventMixin implements TransientCommandBossBar {

    @Unique
    private boolean isTransient = false;

    @Override
    public boolean kibu$isTransient() {
        return isTransient;
    }

    @Override
    public void kibu$setTransient(boolean isTransient) {
        this.isTransient = isTransient;
    }
}
