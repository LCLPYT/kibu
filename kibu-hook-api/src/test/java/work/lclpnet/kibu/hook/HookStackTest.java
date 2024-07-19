package work.lclpnet.kibu.hook;

import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HookStackTest {

    @Test
    void registerHook_root_registers() {
        Hook<Runnable> hook = each();

        AtomicBoolean flag = new AtomicBoolean(false);

        HookStack stack = new HookStack();
        stack.registerHook(hook, () -> flag.set(true));

        hook.invoker().run();

        assertTrue(flag.get());
    }

    @Test
    void unregisterHook_root_registers() {
        Hook<Runnable> hook = each();

        AtomicBoolean flag = new AtomicBoolean(false);

        HookStack stack = new HookStack();

        Runnable cb = () -> flag.set(true);
        stack.registerHook(hook, cb);
        stack.unregisterHook(hook, cb);

        hook.invoker().run();

        assertFalse(flag.get());
    }

    @Nonnull
    private static Hook<Runnable> each() {
        return HookFactory.createArrayBacked(Runnable.class, cbs -> () -> {
            for (var cb : cbs) {
                cb.run();
            }
        });
    }

    @Test
    void pop_root_unregisters() {
        Hook<Runnable> hook = each();

        AtomicBoolean flag = new AtomicBoolean(false);

        HookStack stack = new HookStack();
        stack.registerHook(hook, () -> flag.set(true));

        stack.pop();  // not pushed before, drops current frame nonetheless

        hook.invoker().run();

        assertFalse(flag.get());
    }

    @Test
    void pop_pushed_unregistersFrame() {
        Hook<Runnable> hook = each();

        AtomicBoolean flag1 = new AtomicBoolean(false);
        AtomicBoolean flag2 = new AtomicBoolean(false);

        HookStack stack = new HookStack();

        stack.registerHook(hook, () -> flag1.set(true));

        stack.push();
        stack.registerHook(hook, () -> flag2.set(true));
        stack.pop();

        hook.invoker().run();

        assertTrue(flag1.get());
        assertFalse(flag2.get());
    }

    @Test
    void unload_stackEmpty_currentUnregistered() {
        Hook<Runnable> hook = each();

        AtomicBoolean flag = new AtomicBoolean(false);

        HookStack stack = new HookStack();

        stack.registerHook(hook, () -> flag.set(true));

        stack.unload();

        hook.invoker().run();

        assertFalse(flag.get());
    }

    @Test
    void unload_noneRegistered_stackUnregistered() {
        Hook<Runnable> hook = each();

        AtomicBoolean flag = new AtomicBoolean(false);

        HookStack stack = new HookStack();

        stack.registerHook(hook, () -> flag.set(true));
        stack.push();

        stack.unload();

        hook.invoker().run();

        assertFalse(flag.get());
    }

    @Test
    void unload_registeredAndPushed_stackAndCurrentUnregistered() {
        Hook<Runnable> hook = each();

        AtomicBoolean flag1 = new AtomicBoolean(false);
        AtomicBoolean flag2 = new AtomicBoolean(false);

        HookStack stack = new HookStack();

        stack.registerHook(hook, () -> flag1.set(true));

        stack.push();
        stack.registerHook(hook, () -> flag2.set(true));

        stack.unload();

        hook.invoker().run();

        assertFalse(flag1.get());
        assertFalse(flag2.get());
    }
}