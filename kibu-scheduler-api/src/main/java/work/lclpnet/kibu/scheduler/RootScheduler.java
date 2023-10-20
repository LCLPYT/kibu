package work.lclpnet.kibu.scheduler;

import org.slf4j.Logger;
import work.lclpnet.kibu.scheduler.api.Scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RootScheduler extends Scheduler {

    private final Object mutex = new Object(), offMutex = new Object();
    private Scheduler[] children;
    private volatile List<Scheduler> addQueue = null, removeQueue = null;
    private final ThreadLocal<Boolean> deadlock = ThreadLocal.withInitial(() -> false);

    public RootScheduler(Logger logger) {
        super(logger);
        this.children = new Scheduler[0];
    }

    /**
     * Add a child to this scheduler. The parent will invoke <code>child.tick()</code> on tick.
     *
     * @param child The child to add.
     */
    public void addChild(Scheduler child) {
        if (child == null) throw new NullPointerException("Child must not be null");

        if (Boolean.TRUE.equals(deadlock.get())) {
            synchronized (offMutex) {
                if (addQueue == null) {
                    addQueue = new ArrayList<>();
                }

                addQueue.add(child);
            }

            return;
        }

        synchronized (mutex) {
            var newChildren = new Scheduler[this.children.length + 1];
            System.arraycopy(this.children, 0, newChildren, 0, this.children.length);
            newChildren[this.children.length] = child;

            this.children = newChildren;
        }
    }

    public void addChildren(Collection<? extends Scheduler> children) {
        if (children.isEmpty()) return;

        if (Boolean.TRUE.equals(deadlock.get())) {
            synchronized (offMutex) {
                if (addQueue == null) {
                    addQueue = new ArrayList<>();
                }

                addQueue.addAll(children);
            }

            return;
        }

        synchronized (mutex) {
            var newChildren = new Scheduler[this.children.length + children.size()];
            System.arraycopy(this.children, 0, newChildren, 0, this.children.length);

            int i = this.children.length;

            for (Scheduler scheduler : children) {
                newChildren[i++] = scheduler;
            }

            this.children = newChildren;
        }
    }

    /**
     * Remove a child from this scheduler.
     *
     * @param child The child to remove.
     */
    public void removeChild(Scheduler child) {
        if (child == null) return;

        if (deadlock.get()) {
            synchronized (offMutex) {
                if (removeQueue == null) {
                    removeQueue = new ArrayList<>();
                }

                removeQueue.add(child);
            }

            return;
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

    public void removeChildren(Collection<? extends Scheduler> children) {
        if (children.isEmpty()) return;

        if (deadlock.get()) {
            synchronized (offMutex) {
                if (removeQueue == null) {
                    removeQueue = new ArrayList<>();
                }

                removeQueue.addAll(children);
            }

            return;
        }

        synchronized (mutex) {
            int expected = children.size();
            int[] indices = new int[expected];
            int count = 0;

            for (int i = 0; i < this.children.length && count < expected; i++) {
                if (children.contains(this.children[i])) {
                    indices[count++] = i;
                }
            }

            if (count == 0) return;

            var newChildren = new Scheduler[this.children.length - count];

            int cursor = 0, newCursor = 0;

            for (int i = 0; i < count; i++) {
                int idx = indices[i];

                int len = idx - cursor;

                System.arraycopy(this.children, cursor, newChildren, newCursor, len);

                cursor = idx + 1;
                newCursor += len;
            }

            if (newCursor < newChildren.length) {
                System.arraycopy(this.children, cursor, newChildren, newCursor, newChildren.length - newCursor);
            }

            this.children = newChildren;
        }
    }

    @Override
    public void tick() {
        super.tick();

        synchronized (offMutex) {
            if (addQueue != null) {
                addChildren(addQueue);
                addQueue.clear();
            }

            if (removeQueue != null) {
                removeChildren(removeQueue);
                removeQueue.clear();
            }
        }

        synchronized (mutex) {
            deadlock.set(true);

            for (var child : children) {
                child.tick();
            }

            deadlock.set(false);
        }
    }
}
