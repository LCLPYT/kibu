package work.lclpnet.kibu.hook;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HookContainerTest {

    @Test
    void registerHook() {
        Hook<Runnable> hook = each();

        AtomicBoolean flag = new AtomicBoolean(false);

        HookContainer container = new HookContainer();
        container.registerHook(hook, () -> flag.set(true));

        hook.invoker().run();

        assertTrue(flag.get());
    }

    @Test
    void unregisterHook() {
        Hook<Runnable> hook = each();

        AtomicBoolean flag = new AtomicBoolean(false);

        HookContainer container = new HookContainer();
        Runnable cb = () -> flag.set(true);
        container.registerHook(hook, cb);
        container.unregisterHook(hook, cb);

        hook.invoker().run();

        assertFalse(flag.get());
    }

    @Test
    void registerHooks() {
        Hook<Runnable> hook = each();

        AtomicBoolean flag1 = new AtomicBoolean(false);
        AtomicBoolean flag2 = new AtomicBoolean(false);

        HookContainer container = new HookContainer();
        container.registerHooks(registrar -> {
            registrar.registerHook(hook, () -> flag1.set(true));
            registrar.registerHook(hook, () -> flag2.set(true));
        });

        hook.invoker().run();

        assertTrue(flag1.get());
        assertTrue(flag2.get());
    }

    @Test
    void unload() {
        Hook<Runnable> hook = each();

        AtomicBoolean flag = new AtomicBoolean(false);

        HookContainer container = new HookContainer();
        Runnable cb = () -> flag.set(true);
        container.registerHook(hook, cb);
        container.unload();

        hook.invoker().run();

        assertFalse(flag.get());
    }

    @NotNull
    private static Hook<Runnable> each() {
        return HookFactory.createArrayBacked(Runnable.class, cbs -> () -> {
            for (var cb : cbs) {
                cb.run();
            }
        });
    }
}