package work.lclpnet.kibu.hook;

import javax.annotation.Nullable;
import java.util.Stack;
import java.util.function.Supplier;

public class HookStack implements HookRegistrar {

    private final Supplier<HookRegistrar> factory;
    private HookRegistrar current = null;
    private Stack<HookRegistrar> stack = null;

    public HookStack() {
        this(HookContainer::new);
    }

    public HookStack(Supplier<HookRegistrar> factory) {
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

    protected HookRegistrar current() {
        synchronized (this) {
            if (current == null) {
                current = factory.get();
            }

            return current;
        }
    }

    @Override
    public <T> void registerHook(Hook<T> hook, T listener) {
        current().registerHook(hook, listener);
    }

    @Override
    public <T> void unregisterHook(Hook<T> hook, T listener) {
        current().unregisterHook(hook, listener);
    }

    @Override
    public void registerHooks(HookListenerModule hooks) {
        current().registerHooks(hooks);
    }

    private void maybeUnload(@Nullable HookRegistrar registrar) {
        if (registrar instanceof HookContainer container) {
            container.unload();
        }
    }
}
