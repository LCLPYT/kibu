package work.lclpnet.kibu.scheduler.util;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import work.lclpnet.kibu.scheduler.KibuScheduling;
import work.lclpnet.kibu.scheduler.RootScheduler;
import work.lclpnet.kibu.scheduler.api.*;

import java.util.Stack;
import java.util.function.Supplier;

public class SchedulerStack implements TaskScheduler {

    private final Supplier<Scheduler> factory;
    private Scheduler current = null;
    private Stack<Scheduler> stack = null;

    public SchedulerStack(Logger logger) {
        this(KibuScheduling.getRootScheduler(), logger);
    }

    public SchedulerStack(RootScheduler root, Logger logger) {
        this(() -> new ChildScheduler(root, logger));
    }

    public SchedulerStack(Supplier<Scheduler> factory) {
        this.factory = factory;
    }

    public void push() {
        synchronized (this) {
            if (stack == null) {
                stack = new Stack<>();
            }

            if (current != null) {
                stack.push(current);
            }

            current = null;
        }
    }

    public void pop() {
        synchronized (this) {
            maybeUnload(current);

            if (stack == null || stack.isEmpty()) {
                current = null;
                return;
            }

            current = stack.pop();
        }
    }

    public void unload() {
        synchronized (this) {
            maybeUnload(current);

            if (stack != null) {
                while (!stack.isEmpty()) {
                    var element = stack.pop();
                    maybeUnload(element);
                }
            }

            current = null;
            stack = null;
        }
    }

    /**
     * Get or create the current scheduler in the stack.
     * @return The current scheduler in the stack.
     */
    public Scheduler current() {
        synchronized (this) {
            if (current == null) {
                current = factory.get();
            }

            return current;
        }
    }

    @Override
    public TaskHandle schedule(ScheduledTask task) {
        return current().schedule(task);
    }

    @Override
    public TaskHandle immediate(SchedulerAction action) {
        return current().immediate(action);
    }

    @Override
    public TaskHandle immediate(Runnable action) {
        return current().immediate(action);
    }

    @Override
    public TaskHandle timeout(SchedulerAction action, long timeoutTicks) {
        return current().timeout(action, timeoutTicks);
    }

    @Override
    public TaskHandle timeout(Runnable action, long timeoutTicks) {
        return current().timeout(action, timeoutTicks);
    }

    @Override
    public TaskHandle interval(SchedulerAction action, long intervalTicks) {
        return current().interval(action, intervalTicks);
    }

    @Override
    public TaskHandle interval(Runnable action, long intervalTicks) {
        return current().interval(action, intervalTicks);
    }

    @Override
    public TaskHandle interval(SchedulerAction action, long intervalTicks, long timeoutTicks) {
        return current().interval(action, intervalTicks, timeoutTicks);
    }

    @Override
    public TaskHandle interval(Runnable action, long intervalTicks, long timeoutTicks) {
        return current().interval(action, intervalTicks, timeoutTicks);
    }

    private void maybeUnload(@NotNull Scheduler registrar) {
        if (registrar instanceof ChildScheduler child) {
            child.detach();
        }
    }
}
