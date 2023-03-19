package work.lclpnet.kibu.scheduler.api;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.lclpnet.kibu.scheduler.RootScheduler;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class RootSchedulerTest {

    private static final Logger logger = LoggerFactory.getLogger(RootSchedulerTest.class);

    @Test
    public void addChild() {
        var root = new RootScheduler(logger);
        var child = new Scheduler(logger);
        root.addChild(child);

        var executed = new AtomicBoolean(false);
        child.immediate(() -> executed.set(true));

        root.tick();

        assertTrue(executed.get());
    }

    @Test
    public void removeChild() {
        var root = new RootScheduler(logger);
        var child = new Scheduler(logger);
        root.addChild(child);

        var executed = new AtomicBoolean(false);
        child.immediate(() -> executed.set(true));

        root.removeChild(child);
        root.tick();

        assertFalse(executed.get());
    }

    @Test
    public void testDeadlock() {
        final var root = new RootScheduler(logger);
        final var child = new Scheduler(logger);
        root.addChild(child);

        child.immediate(() -> assertThrows(IllegalStateException.class, () -> root.removeChild(child)));

        root.tick();
    }
}
