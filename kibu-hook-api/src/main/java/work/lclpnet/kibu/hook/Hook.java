package work.lclpnet.kibu.hook;

/**
 * Hooks are very similar to the fabric api {@link net.fabricmc.fabric.api.event.Event}s.
 * Sadly, Fabric API events cannot simply be unregistered.
 * In some use-cases the event listener should be unregistered again, which is exactly what this interface is for.
 *
 * @param <T> The listener type.
 */
public interface Hook<T> {

    T invoker();

    void register(T listener);

    void unregister(T listener);
}
