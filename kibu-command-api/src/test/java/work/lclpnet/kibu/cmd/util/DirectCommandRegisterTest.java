package work.lclpnet.kibu.cmd.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectCommandRegisterTest {

    @Test
    void testRegister() {
        var dispatcher = new CommandDispatcher<String>();
        var register = new DirectCommandRegister<>(dispatcher);

        var cmd = register.register(LiteralArgumentBuilder.literal("test")).join();
        var ref = dispatcher.getRoot().getChild("test");

        assertNotNull(cmd);
        assertEquals(ref, cmd);
    }

    @Test
    void testUnregister() {
        var dispatcher = new CommandDispatcher<String>();
        var register = new DirectCommandRegister<>(dispatcher);

        var cmd = register.register(LiteralArgumentBuilder.literal("test")).join();
        assertNotNull(dispatcher.getRoot().getChild("test"));

        assertTrue(register.unregister(cmd));
        assertNull(dispatcher.getRoot().getChild("test"));
    }
}