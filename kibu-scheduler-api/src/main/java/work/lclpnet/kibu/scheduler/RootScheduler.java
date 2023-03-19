package work.lclpnet.kibu.scheduler;

import org.slf4j.Logger;
import work.lclpnet.kibu.scheduler.api.Scheduler;

public class RootScheduler extends Scheduler {

    private final Object mutex = new Object();
    private Scheduler[] children;
    private final ThreadLocal<Boolean> deadlock = ThreadLocal.withInitial(() -> false);

    public RootScheduler(Logger logger) {
        super(logger);
        this.children = new Scheduler[0];
    }

    /**
     * Add a child to this scheduler. The parent will invoke <code>child.tick()</code> on tick.
     * Children may not modify their parent's children from their <code>tick()</code> method.
     * Use {@link RootScheduler#immediate(Runnable)} of the parent instead.
     *
     * @param child The child to add.
     */
    public void addChild(Scheduler child) {
        if (child == null) throw new NullPointerException("Child must not be null");

        if (Boolean.TRUE.equals(deadlock.get())) {
            throw new IllegalStateException("Modification of root scheduler during iteration (deadlock)");
        }

        synchronized (mutex) {
            var newChildren = new Scheduler[this.children.length + 1];
            System.arraycopy(this.children, 0, newChildren, 0, this.children.length);
            newChildren[this.children.length] = child;

            this.children = newChildren;
        }
    }

    /**
     * Remove a child from this scheduler.
     * Children may not modify their parent's children from their <code>tick()</code> method.
     * Use {@link RootScheduler#immediate(Runnable)} of the parent instead.
     *
     * @param child The child to remove.
     */
    public void removeChild(Scheduler child) {
        if (child == null) return;

        if (Boolean.TRUE.equals(deadlock.get())) {
            throw new IllegalStateException("Modification of root scheduler during iteration (deadlock)");
        }

        synchronized (mutex) {
            int idx = -1;

            for (int i = 0; i < this.children.length; i++) {
                if (child.equals(this.children[i])) {
                    idx = i;
                    break;
                }
            }

            if (idx == -1) return;

            var newChildren = new Scheduler[this.children.length - 1];
            System.arraycopy(this.children, 0, newChildren, 0, idx);
            System.arraycopy(this.children, idx + 1, newChildren, idx, this.children.length - idx - 1);

            this.children = newChildren;
        }
    }

    @Override
    public void tick() {
        super.tick();

        synchronized (mutex) {
            deadlock.set(true);

            for (var child : children) {
                child.tick();
            }

            deadlock.set(false);
        }
    }
}
