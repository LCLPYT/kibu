package work.lclpnet.kibu.cmd.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.cmd.type.CommandConsumer;

import static org.junit.jupiter.api.Assertions.*;

class DirectCommandRegisterTest {

    @Test
    void testRegister() {
        var dispatcher = new CommandDispatcher<String>();
        var register = new DirectCommandRegister<>(dispatcher, new CommandRegistryAccessMock(), CommandManager.RegistrationEnvironment.DEDICATED);

        var consumer = new TestConsumer();
        assertTrue(register.register(LiteralArgumentBuilder.literal("test"), consumer));

        consumer.assertEqualsCommand(dispatcher, "test");
    }

    @Test
    void testRegisterFactory() {
        var dispatcher = new CommandDispatcher<String>();
        var register = new DirectCommandRegister<>(dispatcher, new CommandRegistryAccessMock(), CommandManager.RegistrationEnvironment.DEDICATED);

        var consumer = new TestConsumer();
        assertTrue(register.register(context -> LiteralArgumentBuilder.literal("test"), consumer));

        consumer.assertEqualsCommand(dispatcher, "test");
    }

    @Test
    void testUnregister() {
        var dispatcher = new CommandDispatcher<String>();
        var register = new DirectCommandRegister<>(dispatcher, new CommandRegistryAccessMock(), CommandManager.RegistrationEnvironment.DEDICATED);

        var consumer = new TestConsumer();
        assertTrue(register.register(LiteralArgumentBuilder.literal("test"), consumer));
        assertNotNull(dispatcher.getRoot().getChild("test"));

        assertTrue(register.unregister(consumer.command));
        assertNull(dispatcher.getRoot().getChild("test"));
    }

    private static class TestConsumer implements CommandConsumer<String> {

        private LiteralCommandNode<String> command;

        @Override
        public void acceptCommand(LiteralCommandNode<String> command) {
            this.command = command;
        }

        public void assertEqualsCommand(CommandDispatcher<String> dispatcher, String name) {
            var ref = dispatcher.getRoot().getChild(name);
            assertNotNull(ref);
            assertEquals(ref, command, "Command is not registered on the dispatcher");
        }
    }
}