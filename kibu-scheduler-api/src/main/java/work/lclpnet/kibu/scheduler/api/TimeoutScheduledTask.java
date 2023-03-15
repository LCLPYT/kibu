package work.lclpnet.kibu.scheduler.api;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TimeoutScheduledTask implements ScheduledTask {

    @Nonnull
    private final SchedulerAction runnable;
    private final Object mutex = new Object();
    private List<Runnable> whenComplete = null;
    private long timeout = 0;
    private long interval = 0;
    private boolean active = true;

    public TimeoutScheduledTask(SchedulerAction action) {
        this.runnable = Objects.requireNonNull(action);
    }

    public TimeoutScheduledTask setTimeout(long timeoutTicks) {
        timeout = timeoutTicks;
        return this;
    }

    public TimeoutScheduledTask setInterval(long intervalTicks) {
        if (intervalTicks == 0) throw new IllegalArgumentException("Interval ticks might not be zero");
        interval = intervalTicks;
        return this;
    }

    @Override
    public boolean tick() {
        boolean done = false;

        synchronized (mutex) {
            if (!active) {
                done = true;
            }
        }

        if (done) {
            // task is over
            onFinish();
            return true;
        }

        if (timeout-- != 0) return false;
        if (interval > 0) timeout = interval - 1;

        runnable.run(this);

        // task is over, when timeout is exactly -1 (=> 0--)
        done = !active || timeout == -1;

        if (done) {
            onFinish();
        }

        return done;
    }

    private void onFinish() {
        if (whenComplete == null) return;

        whenComplete.forEach(Runnable::run);
    }

    @Override
    public void cancel() {
        synchronized (mutex) {
            active = false;
        }
    }

    @Override
    public void addWhenComplete(Runnable action) {
        if (whenComplete == null) whenComplete = new ArrayList<>();

        whenComplete.add(action);
    }
}
