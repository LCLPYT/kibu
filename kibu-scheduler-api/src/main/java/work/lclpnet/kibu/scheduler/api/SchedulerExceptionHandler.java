package work.lclpnet.kibu.scheduler.api;

public interface SchedulerExceptionHandler {

    /**
     * Handle an exception thrown by a scheduler task.
     * @param throwable The thrown exception.
     * @return Whether the exception handler could recover from the exception.
     * If false is returned, the task will be stopped.
     */
    boolean handleException(Throwable throwable);
}
