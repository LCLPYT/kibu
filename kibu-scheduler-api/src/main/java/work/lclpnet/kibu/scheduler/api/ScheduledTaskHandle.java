package work.lclpnet.kibu.scheduler.api;

import java.util.Objects;

public class ScheduledTaskHandle implements TaskHandle {

    private final ScheduledTask task;

    public ScheduledTaskHandle(ScheduledTask task) {
        this.task = Objects.requireNonNull(task);
    }

    @Override
    public void cancel() {
        task.cancel();
    }

    @Override
    public TaskHandle whenComplete(Runnable action) {
        task.addWhenComplete(action);
        return this;
    }
}
