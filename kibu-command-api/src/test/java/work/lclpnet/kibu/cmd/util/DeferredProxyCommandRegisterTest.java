package work.lclpnet.kibu.cmd.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeferredProxyCommandRegisterTest {

    @Test
    void testDeferredRegister() {
        var proxy = new DeferredProxyCommandRegister<String>();

        var future = proxy.register(LiteralArgumentBuilder.literal("test"));
        assertFalse(future.isDone());

        var dispatcher = new CommandDispatcher<String>();
        var target = new DirectCommandRegister<>(dispatcher);
        proxy.setTarget(target);  // will register deferred

        assertTrue(future.isDone());

        var cmd = future.getNow(null);
        assertNotNull(cmd);

        var ref = dispatcher.getRoot().getChild("test");
        assertEquals(ref, cmd);

        // now commands should register immediately
        future = proxy.register(LiteralArgumentBuilder.literal("bar"));
        assertTrue(future.isDone());

        cmd = future.join();
        assertNotNull(cmd);

        ref = dispatcher.getRoot().getChild("bar");
        assertEquals(ref, cmd);
    }
}