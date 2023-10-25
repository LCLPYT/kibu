package work.lclpnet.kibu.scheduler.api;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

public class SchedulerTest {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerTest.class);

    @Test
    public void testImmediate() {
        assertExecCount(Scheduler::immediate, 1, 1);
    }

    @Test
    public void testTimeoutImmediate() {
        assertExecCount((scheduler, runnable) -> scheduler.timeout(runnable, 0), 1, 1);
    }

    @Test
    public void testTimeout() {
        assertExecCount((scheduler, runnable) -> scheduler.timeout(runnable, 1), 1, 0);
        assertExecCount((scheduler, runnable) -> scheduler.timeout(runnable, 1), 2, 1);
    }

    @Test
    public void testInterval() {
        assertExecCount((scheduler, runnable) -> scheduler.interval(runnable, 5), 20, 4);
    }

    @Test
    public void testIntervalWithTimeout() {
        assertExecCount((scheduler, runnable) -> scheduler.interval(runnable, 5, 10), 20, 2);
    }

    @Test
    public void testWhenComplete() {
        var executed = new AtomicBoolean();

        assertExecCount((scheduler, runnable) -> scheduler.timeout(runnable, 3)
                .whenComplete(() -> executed.set(true)), 4, 1);

        assertTrue(executed.get());
    }

    @Test
    public void testIntervalCancel() {
        var counter = new AtomicInteger();
        var executed = new AtomicBoolean();

        assertExecCount((scheduler, runnable) -> scheduler.interval(info -> {
            runnable.run();
            counter.set(counter.get() + 1);

            if (counter.get() == 3) info.cancel();
        }, 5).whenComplete(() -> executed.set(true)), 20, 3);

        assertTrue(executed.get());
    }

    @Test
    public void testIntervalCancelByHandle() {
        var executed = new AtomicBoolean();

        // instantly cancel execution
        assertExecCount((scheduler, runnable) -> scheduler.interval(runnable, 5)
                .whenComplete(() -> executed.set(true))
                .cancel(), 20, 0);

        assertTrue(executed.get());
    }

    @Test
    public void testExceptionHandlerHandledContinues() {
        var scheduler = new Scheduler(logger);
        var handled = new AtomicReference<Throwable>(null);
        var executed = new AtomicBoolean(false);

        scheduler.setExceptionHandler(throwable -> {
            handled.set(throwable);
            return true;
        });

        RuntimeException test = new RuntimeException("test");

        scheduler.interval(new SchedulerAction() {
            int t = 0;

            @Override
            public void run(RunningTask info) {
                int tick = t++;

                if (tick == 0) {
                    throw test;
                } else if (tick == 1) {
                    executed.set(true);
                    info.cancel();
                }
            }
        }, 1);

        scheduler.tick();

        assertEquals(test, handled.get());

        scheduler.tick();

        assertTrue(executed.get());
    }

    @Test
    public void testExceptionHandlerNotHandledAborts() {
        var scheduler = new Scheduler(logger);
        var handled = new AtomicReference<Throwable>(null);
        var executed = new AtomicBoolean(false);

        scheduler.setExceptionHandler(throwable -> {
            handled.set(throwable);
            return false;
        });

        RuntimeException test = new RuntimeException("test");

        scheduler.interval(new SchedulerAction() {
            int t = 0;

            @Override
            public void run(RunningTask info) {
                int tick = t++;

                if (tick == 0) {
                    throw test;
                } else if (tick == 1) {
                    executed.set(true);
                    info.cancel();
                }
            }
        }, 1);

        scheduler.tick();

        assertEquals(test, handled.get());

        scheduler.tick();

        assertFalse(executed.get());
    }

    private static void assertExecCount(BiConsumer<Scheduler, Runnable> function, int simulatedTicks, int expectedExecCount) {
        var scheduler = new Scheduler(logger);

        var execCounter = new AtomicInteger();
        function.accept(scheduler, () -> execCounter.set(execCounter.get() + 1));

        for (int i = 0; i < simulatedTicks; i++) {
            scheduler.tick();
        }

        assertEquals(expectedExecCount, execCounter.get());
    }
}
