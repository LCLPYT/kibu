package work.lclpnet.kibu.scheduler.api;

public interface TaskHandle {

    void cancel();

    TaskHandle whenComplete(Runnable action);
}
