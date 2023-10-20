package work.lclpnet.kibu.scheduler.api;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.lclpnet.kibu.scheduler.RootScheduler;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void addChildren() {
        var root = new RootScheduler(logger);
        var child1 = new Scheduler(logger);
        var child2 = new Scheduler(logger);

        root.addChildren(List.of(child1, child2));

        var flag1 = new AtomicBoolean(false);
        var flag2 = new AtomicBoolean(false);

        child1.immediate(() -> flag1.set(true));
        child2.immediate(() -> flag2.set(true));

        root.tick();

        assertTrue(flag1.get());
        assertTrue(flag2.get());
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
    public void removeChildren_all() {
        var root = new RootScheduler(logger);
        var child1 = new Scheduler(logger);
        var child2 = new Scheduler(logger);
        root.addChild(child1);
        root.addChild(child2);

        var flag1 = new AtomicBoolean(false);
        var flag2 = new AtomicBoolean(false);

        child1.immediate(() -> flag1.set(true));
        child2.immediate(() -> flag2.set(true));

        root.removeChildren(Set.of(child1, child2));
        root.tick();

        assertFalse(flag1.get());
        assertFalse(flag2.get());
    }

    @Test
    public void removeChildren_trailing() {
        var root = new RootScheduler(logger);
        Scheduler child1 = new Scheduler(logger), child2 = new Scheduler(logger), child3 = new Scheduler(logger);
        root.addChildren(List.of(child1, child2, child3));

        AtomicBoolean flag1 = new AtomicBoolean(false),
                flag2 = new AtomicBoolean(false),
                flag3 = new AtomicBoolean(false);

        child1.immediate(() -> flag1.set(true));
        child2.immediate(() -> flag2.set(true));
        child3.immediate(() -> flag3.set(true));

        root.removeChildren(Set.of(child1, child2));
        root.tick();

        assertFalse(flag1.get());
        assertFalse(flag2.get());
        assertTrue(flag3.get());
    }

    @Test
    public void removeChildren_middle() {
        var root = new RootScheduler(logger);
        Scheduler child1 = new Scheduler(logger), child2 = new Scheduler(logger), child3 = new Scheduler(logger);
        root.addChildren(List.of(child1, child2, child3));

        AtomicBoolean flag1 = new AtomicBoolean(false),
                flag2 = new AtomicBoolean(false),
                flag3 = new AtomicBoolean(false);

        child1.immediate(() -> flag1.set(true));
        child2.immediate(() -> flag2.set(true));
        child3.immediate(() -> flag3.set(true));

        root.removeChildren(Set.of(child1, child3));
        root.tick();

        assertFalse(flag1.get());
        assertTrue(flag2.get());
        assertFalse(flag3.get());
    }

    @Test
    public void removeChildren_preTrailing() {
        var root = new RootScheduler(logger);
        Scheduler child1 = new Scheduler(logger), child2 = new Scheduler(logger), child3 = new Scheduler(logger);
        root.addChildren(List.of(child1, child2, child3));

        AtomicBoolean flag1 = new AtomicBoolean(false),
                flag2 = new AtomicBoolean(false),
                flag3 = new AtomicBoolean(false);

        child1.immediate(() -> flag1.set(true));
        child2.immediate(() -> flag2.set(true));
        child3.immediate(() -> flag3.set(true));

        root.removeChildren(Set.of(child2, child3));
        root.tick();

        assertTrue(flag1.get());
        assertFalse(flag2.get());
        assertFalse(flag3.get());
    }

    @Test
    public void addChild_testCoModification() {
        final var root = new RootScheduler(logger);
        final var child1 = new Scheduler(logger);
        final var child2 = new Scheduler(logger);
        root.addChild(child1);

        AtomicBoolean flag1 = new AtomicBoolean();
        AtomicBoolean flag2 = new AtomicBoolean();

        child1.immediate(() -> {
            flag1.set(true);
            root.addChild(child2);
        });

        child2.immediate(() -> flag2.set(true));

        root.tick();

        assertTrue(flag1.get());
        assertFalse(flag2.get());

        root.tick();

        assertTrue(flag2.get());
    }

    @Test
    public void removeChild_testCoModification() {
        final var root = new RootScheduler(logger);
        final var child1 = new Scheduler(logger);
        final var child2 = new Scheduler(logger);
        root.addChild(child1);
        root.addChild(child2);

        AtomicBoolean flag1 = new AtomicBoolean();
        AtomicBoolean flag2 = new AtomicBoolean();

        child1.immediate(() -> {
            flag1.set(true);
            root.removeChild(child2);
        });

        child2.immediate(() -> flag2.set(true));

        root.tick();

        assertTrue(flag1.get());
        assertTrue(flag2.get());

        flag2.set(false);

        root.tick();

        assertFalse(flag2.get());
    }
}
