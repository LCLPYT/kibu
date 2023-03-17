package work.lclpnet.kibu.hook;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.function.Function;

public class ArrayBackedHook<T> implements Hook<T> {

    private final Function<T[], T> invokerBuilder;
    private final Object mutex = new Object();
    private volatile T invoker;
    private T[] listeners;

    @SuppressWarnings("unchecked")
    ArrayBackedHook(Class<T> type, Function<T[], T> invokerBuilder) {
        this.invokerBuilder = Objects.requireNonNull(invokerBuilder);
        this.listeners = (T[]) Array.newInstance(type, 0);
        rebuildInvoker();
    }

    private void rebuildInvoker() {
        this.invoker = this.invokerBuilder.apply(listeners);
    }

    @Override
    public T invoker() {
        return invoker;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void register(T listener) {
        if (listener == null) throw new NullPointerException("Listener might not be null");

        synchronized (mutex) {
            T[] extListeners = (T[]) Array.newInstance(listeners.getClass().getComponentType(), listeners.length + 1);
            System.arraycopy(listeners, 0, extListeners, 0, listeners.length);
            extListeners[listeners.length] = listener;

            listeners = extListeners;

            rebuildInvoker();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void unregister(T listener) {
        if (listener == null) return;

        synchronized (mutex) {
            int idx = -1;

            for (int i = 0; i < listeners.length; i++) {
                if (listener.equals(listeners[i])) {
                    idx = i;
                    break;
                }
            }

            if (idx == -1) return;

            T[] redListeners = (T[]) Array.newInstance(listeners.getClass().getComponentType(), listeners.length - 1);
            System.arraycopy(listeners, 0, redListeners, 0, idx);
            System.arraycopy(listeners, idx + 1, redListeners, idx, listeners.length - idx - 1);

            listeners = redListeners;

            rebuildInvoker();
        }
    }
}
