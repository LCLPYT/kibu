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
}
