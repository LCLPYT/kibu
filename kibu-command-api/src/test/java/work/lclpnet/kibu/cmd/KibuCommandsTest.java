package work.lclpnet.kibu.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.cmd.type.CommandRegister;
import work.lclpnet.kibu.cmd.util.CommandDispatcherUtils;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static org.junit.jupiter.api.Assertions.*;

public class KibuCommandsTest {

    @Test
    public void testRegisterWithoutBootstrap() {
        var register = new TestCommandRegister();
        new KibuCommands<>(register).onInitialize();

        var cmd = KibuCommands.register(literal("test"));
        var registered = register.dispatcher.getRoot().getChild("test");

        assertNotNull(cmd);
        assertEquals(registered, cmd);
    }

    @Test
    public void testUnRegisterWithoutBootstrap() {
        var register = new TestCommandRegister();
        new KibuCommands<>(register).onInitialize();

        var cmd = KibuCommands.register(literal("test"));
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
        public LiteralCommandNode<String> register(LiteralArgumentBuilder<String> command) {
            return CommandDispatcherUtils.register(dispatcher, command);
        }

        @Override
        public void unregister(LiteralCommandNode<String> command) {
            CommandDispatcherUtils.unregister(dispatcher, command);
        }
    }
}
