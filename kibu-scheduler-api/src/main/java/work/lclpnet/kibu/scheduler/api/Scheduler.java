package work.lclpnet.kibu.scheduler.api;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Scheduler {

    private final List<ScheduledTask> tasks = new ArrayList<>();
    private final List<ScheduledTask> upcomingTasks = new ArrayList<>();
    private final Logger logger;

    public Scheduler(Logger logger) {
        this.logger = logger;
    }

    public TaskHandle schedule(ScheduledTask task) {
        Objects.requireNonNull(task);

        synchronized (upcomingTasks) {
            upcomingTasks.add(task);
        }

        if (task instanceof TaskHandle handle) {
            return handle;
        }

        return new ScheduledTaskHandle(task);
    }

    public void tick() {
        // add upcoming tasks
        synchronized (upcomingTasks) {
            if (!upcomingTasks.isEmpty()) {
                tasks.addAll(upcomingTasks);
                upcomingTasks.clear();
            }
        }

        // tick tasks and remove them, if they are done
        tasks.removeIf(scheduledTask -> {
            boolean done;
            try {
                done = scheduledTask.tick();
            } catch (Throwable t) {
                logger.error("Error executing scheduler task", t);
                return true;
            }
            return done;
        });
    }

    public TimeoutScheduledTask createTask(SchedulerAction action) {
        return new TimeoutScheduledTask(action);
    }

    public TimeoutScheduledTask createTask(Runnable action) {
        return new TimeoutScheduledTask(handle -> action.run());
    }

    public TaskHandle immediate(SchedulerAction action) {
        return schedule(createTask(action));
    }

    public TaskHandle immediate(Runnable action) {
        return schedule(createTask(action));
    }

    public TaskHandle timeout(SchedulerAction action, long timeoutTicks) {
        return schedule(createTask(action).setTimeout(timeoutTicks));
    }

    public TaskHandle timeout(Runnable action, long timeoutTicks) {
        return schedule(createTask(action).setTimeout(timeoutTicks));
    }

    public TaskHandle interval(SchedulerAction action, long intervalTicks) {
        return interval(action, intervalTicks, 0);
    }

    public TaskHandle interval(Runnable action, long intervalTicks) {
        return interval(action, intervalTicks, 0);
    }

    public TaskHandle interval(SchedulerAction action, long intervalTicks, long timeoutTicks) {
        return schedule(createTask(action).setTimeout(timeoutTicks).setInterval(intervalTicks));
    }

    public TaskHandle interval(Runnable action, long intervalTicks, long timeoutTicks) {
        return schedule(createTask(action).setTimeout(timeoutTicks).setInterval(intervalTicks));
    }
}
