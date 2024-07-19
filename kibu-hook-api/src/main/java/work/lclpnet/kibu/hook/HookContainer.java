package work.lclpnet.kibu.hook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HookContainer implements HookRegistrar {

    private final Object mutex = new Object();
    private final Map<Hook<?>, List<?>> eventListeners = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public final <T> void registerHook(Hook<T> hook, T listener) {
        if (hook == null || listener == null) return;

        synchronized (mutex) {
            var listeners = (List<T>) eventListeners.computeIfAbsent(hook, h -> new ArrayList<T>());
            listeners.add(listener);
        }

        hook.register(listener);
    }

    @Override
    public final <T> void unregisterHook(Hook<T> hook, T listener) {
        if (listener == null) return;

        hook.unregister(listener);

        synchronized (mutex) {
            var listeners = eventListeners.get(hook);
            if (listeners == null) return;

            listeners.remove(listener);
        }
    }

    @Override
    public void registerHooks(HookListenerModule hooks) {
        if (hooks != null) {
            hooks.registerListeners(this);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void unregisterListenersOf(Hook<T> hook) {
        var listeners = (List<T>) eventListeners.get(hook);
        listeners.forEach(hook::unregister);
    }

    public void unload() {
        synchronized (mutex) {
            for (Hook<?> hook : eventListeners.keySet()) {
                unregisterListenersOf(hook);
            }

            eventListeners.clear();
        }
    }
}
