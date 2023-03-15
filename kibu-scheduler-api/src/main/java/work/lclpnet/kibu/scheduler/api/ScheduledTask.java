package work.lclpnet.kibu.scheduler.api;

public interface ScheduledTask extends RunningTask {

    boolean tick();

    void addWhenComplete(Runnable action);
}
