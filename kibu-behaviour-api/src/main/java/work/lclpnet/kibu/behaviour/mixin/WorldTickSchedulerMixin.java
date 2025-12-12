package work.lclpnet.kibu.behaviour.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.ticks.LevelTicks;
import net.minecraft.world.ticks.ScheduledTick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.behaviour.type.KibuTickScheduler;

import java.util.function.BiConsumer;

@Mixin(LevelTicks.class)
public abstract class WorldTickSchedulerMixin implements KibuTickScheduler {

    @Shadow protected abstract void cleanupAfterTick();

    @Unique
    private boolean enabled = true;

    @Override
    public void kibu$setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean kibu$isEnabled() {
        return enabled;
    }

    @Inject(
            method = "tick(JILjava/util/function/BiConsumer;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$beforeTick(long time, int maxTicks, BiConsumer<BlockPos, Object> ticker, CallbackInfo ci) {
        if (enabled) return;

        ci.cancel();
        this.cleanupAfterTick();
    }

    @Inject(
            method = "schedule",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onSchedule(ScheduledTick<Object> orderedTick, CallbackInfo ci) {
        if (!enabled) {
            ci.cancel();
        }
    }
}
