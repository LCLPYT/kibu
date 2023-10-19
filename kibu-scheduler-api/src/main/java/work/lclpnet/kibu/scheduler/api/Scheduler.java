package work.lclpnet.kibu.scheduler.api;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Scheduler implements TaskScheduler {

    private final List<ScheduledTask> tasks = new ArrayList<>();
    private final List<ScheduledTask> upcomingTasks = new ArrayList<>();
    private final Logger logger;

    public Scheduler(Logger logger) {
        this.logger = logger;
    }

    @Override
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

    @Override
    public TaskHandle immediate(SchedulerAction action) {
        return schedule(createTask(action));
    }

    @Override
    public TaskHandle immediate(Runnable action) {
        return schedule(createTask(action));
    }

    @Override
    public TaskHandle timeout(SchedulerAction action, long timeoutTicks) {
        return schedule(createTask(action).setTimeout(timeoutTicks));
    }

    @Override
    public TaskHandle timeout(Runnable action, long timeoutTicks) {
        return schedule(createTask(action).setTimeout(timeoutTicks));
    }

    @Override
    public TaskHandle interval(SchedulerAction action, long intervalTicks) {
        return interval(action, intervalTicks, 0);
    }

    @Override
    public TaskHandle interval(Runnable action, long intervalTicks) {
        return interval(action, intervalTicks, 0);
    }

    @Override
    public TaskHandle interval(SchedulerAction action, long intervalTicks, long timeoutTicks) {
        return schedule(createTask(action).setTimeout(timeoutTicks).setInterval(intervalTicks));
    }

    @Override
    public TaskHandle interval(Runnable action, long intervalTicks, long timeoutTicks) {
        return schedule(createTask(action).setTimeout(timeoutTicks).setInterval(intervalTicks));
    }
}
