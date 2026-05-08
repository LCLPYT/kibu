package work.lclpnet.kibu.hook;

/**
 * Hooks are very similar to the fabric api {@link net.fabricmc.fabric.api.event.Event}s.
 * Sadly, Fabric API events cannot simply be unregistered.
 * In some use-cases the event listener should be unregistered again, which is exactly what this interface is for.
 *
 * @param <T> The listener type.
 */
public interface Hook<T> extends Registrable<T>, Unregistrable<T>, Invocable<T> {

    /**
     * Registers a listener through a given {@link HookRegistrar}.
     * This just calls the {@link HookRegistrar#registerHook(Hook, Object)} method which in turn should call {@link #register(Object)} on this hook normally.
     * Exists mostly as a bridge for Kotlin to pin the SAM type.
     * @param registrar The registrar that performs the registration.
     * @param listener The listener to register.
     */
    default void registerWith(HookRegistrar registrar, T listener) {
        registrar.registerHook(this, listener);
    }
}
