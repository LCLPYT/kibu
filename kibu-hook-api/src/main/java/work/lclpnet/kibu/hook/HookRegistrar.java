package work.lclpnet.kibu.hook;

public interface HookRegistrar {

    <T> void registerHook(Hook<T> hook, T listener);

    <T> void unregisterHook(Hook<T> hook, T listener);

    void registerHooks(HookListenerModule hooks);
}
