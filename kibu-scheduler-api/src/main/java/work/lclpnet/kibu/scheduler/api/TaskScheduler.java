package work.lclpnet.kibu.scheduler.api;

public interface TaskScheduler {

    TaskHandle schedule(ScheduledTask task);

    TaskHandle immediate(SchedulerAction action);

    TaskHandle immediate(Runnable action);

    TaskHandle timeout(SchedulerAction action, long timeoutTicks);

    TaskHandle timeout(Runnable action, long timeoutTicks);

    TaskHandle interval(SchedulerAction action, long intervalTicks);

    TaskHandle interval(Runnable action, long intervalTicks);

    TaskHandle interval(SchedulerAction action, long intervalTicks, long timeoutTicks);

    TaskHandle interval(Runnable action, long intervalTicks, long timeoutTicks);

    // Kotlin friendly methods:

    default TaskHandle timeout(int timeoutTicks, SchedulerAction action) {
        return timeout(action, timeoutTicks);
    }

    default TaskHandle timeout(long timeoutTicks, SchedulerAction action) {
        return timeout(action, timeoutTicks);
    }

    default TaskHandle timeout(int timeoutTicks, Runnable action) {
        return timeout(action, timeoutTicks);
    }

    default TaskHandle timeout(long timeoutTicks, Runnable action) {
        return timeout(action, timeoutTicks);
    }

    default TaskHandle interval(int intervalTicks, SchedulerAction action) {
        return interval(action, intervalTicks);
    }

    default TaskHandle interval(long intervalTicks, SchedulerAction action) {
        return interval(action, intervalTicks);
    }

    default TaskHandle interval(int intervalTicks, Runnable action) {
        return interval(action, intervalTicks);
    }

    default TaskHandle interval(long intervalTicks, Runnable action) {
        return interval(action, intervalTicks);
    }

    default TaskHandle interval(int intervalTicks, int timeoutTicks, SchedulerAction action) {
        return interval(action, intervalTicks, timeoutTicks);
    }

    default TaskHandle interval(long intervalTicks, long timeoutTicks, SchedulerAction action) {
        return interval(action, intervalTicks, timeoutTicks);
    }

    default TaskHandle interval(int intervalTicks, int timeoutTicks, Runnable action) {
        return interval(action, intervalTicks, timeoutTicks);
    }

    default TaskHandle interval(long intervalTicks, long timeoutTicks, Runnable action) {
        return interval(action, intervalTicks, timeoutTicks);
    }
}
