package work.lclpnet.kibu.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.cmd.type.CommandRegister;
import work.lclpnet.kibu.cmd.util.CommandDispatcherUtils;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static org.junit.jupiter.api.Assertions.*;

public class KibuCommandsTest {

    @Test
    public void testRegisterWithoutBootstrap() throws ReflectiveOperationException {
        var register = new TestCommandRegister();
        reloadKibuCommands(register);

        var cmd = KibuCommands.register(literal("test")).join();
        var registered = register.dispatcher.getRoot().getChild("test");

        assertNotNull(cmd);
        assertEquals(registered, cmd);
    }

    private static void reloadKibuCommands(TestCommandRegister register) throws ReflectiveOperationException {
        // hack: clear previous instance
        Field instance = KibuCommands.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);  // static

        new KibuCommands<>(register).onInitialize();
    }

    @Test
    public void testUnRegisterWithoutBootstrap() throws ReflectiveOperationException {
        var register = new TestCommandRegister();
        reloadKibuCommands(register);

        var cmd = KibuCommands.register(literal("test")).join();
        var registered = register.dispatcher.getRoot().getChild("test");

        assertNotNull(cmd);
        assertEquals(registered, cmd);

        KibuCommands.unregister(cmd);

        registered = register.dispatcher.getRoot().getChild("test");
        assertNull(registered);
    }

    private static class TestCommandRegister implements CommandRegister<String> {

        private final CommandDispatcher<String> dispatcher = new CommandDispatcher<>();

        @Override
        public CompletableFuture<LiteralCommandNode<String>> register(LiteralArgumentBuilder<String> command) {
            var cmd = CommandDispatcherUtils.register(dispatcher, command);
            return CompletableFuture.completedFuture(cmd);
        }

        @Override
        public void unregister(LiteralCommandNode<String> command) {
            CommandDispatcherUtils.unregister(dispatcher, command);
        }
    }
}
