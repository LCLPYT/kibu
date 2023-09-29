package work.lclpnet.kibu.cmd.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import work.lclpnet.kibu.cmd.type.CommandConsumer;

import static org.junit.jupiter.api.Assertions.*;

class DeferredProxyCommandRegisterTest {

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void registerBuilder_noTarget_notRegistered(boolean keepRefs) {
        var proxy = new DeferredProxyCommandRegister<String>(keepRefs);

        var consumer = new TestConsumer();
        boolean registered = proxy.register(LiteralArgumentBuilder.literal("test"), consumer);

        assertFalse(registered);
        consumer.assertEmpty();
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void registerFactory_noTarget_notRegistered(boolean keepRefs) {
        var proxy = new DeferredProxyCommandRegister<String>(keepRefs);

        var consumer = new TestConsumer();
        boolean registered = proxy.register(ctx -> LiteralArgumentBuilder.literal("test"), consumer);

        assertFalse(registered);
        consumer.assertEmpty();
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void registerBuilder_setTarget_deferredRegister(boolean keepRefs) {
        var proxy = new DeferredProxyCommandRegister<String>(keepRefs);

        var consumer = new TestConsumer();
        proxy.register(LiteralArgumentBuilder.literal("test"), consumer);

        var dispatcher = new CommandDispatcher<String>();
        var target = new DirectCommandRegister<>(dispatcher, new CommandRegistryAccessMock(), CommandManager.RegistrationEnvironment.DEDICATED);
        proxy.setTarget(target);  // will register deferred

        consumer.assertEqualsCommand(dispatcher, "test");
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void registerFactory_setTarget_deferredRegister(boolean keepRefs) {
        var proxy = new DeferredProxyCommandRegister<String>(keepRefs);

        var consumer = new TestConsumer();
        proxy.register(ctx -> LiteralArgumentBuilder.literal("test"), consumer);

        var dispatcher = new CommandDispatcher<String>();
        var target = new DirectCommandRegister<>(dispatcher, new CommandRegistryAccessMock(), CommandManager.RegistrationEnvironment.DEDICATED);
        proxy.setTarget(target);  // will register deferred

        consumer.assertEqualsCommand(dispatcher, "test");
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void registerBuilder_hasTarget_directlyRegistered(boolean keepRefs) {
        var proxy = new DeferredProxyCommandRegister<String>(keepRefs);

        var dispatcher = new CommandDispatcher<String>();
        var target = new DirectCommandRegister<>(dispatcher, new CommandRegistryAccessMock(), CommandManager.RegistrationEnvironment.DEDICATED);
        proxy.setTarget(target);

        var consumer = new TestConsumer();
        boolean registered = proxy.register(LiteralArgumentBuilder.literal("test"), consumer);
        assertTrue(registered);

        consumer.assertEqualsCommand(dispatcher, "test");
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void registerFactory_hasTarget_directlyRegistered(boolean keepRefs) {
        var proxy = new DeferredProxyCommandRegister<String>(keepRefs);

        var dispatcher = new CommandDispatcher<String>();
        var target = new DirectCommandRegister<>(dispatcher, new CommandRegistryAccessMock(), CommandManager.RegistrationEnvironment.DEDICATED);
        proxy.setTarget(target);

        var consumer = new TestConsumer();
        boolean registered = proxy.register(ctx -> LiteralArgumentBuilder.literal("test"), consumer);
        assertTrue(registered);

        consumer.assertEqualsCommand(dispatcher, "test");
    }

    @Test
    void setTarget_flushRefs_deferredFlushed() {
        var proxy = new DeferredProxyCommandRegister<String>(false);

        var consumer = new TestConsumer();
        proxy.register(LiteralArgumentBuilder.literal("test"), consumer);

        var dispatcher = new CommandDispatcher<String>();
        var target = new DirectCommandRegister<>(dispatcher, new CommandRegistryAccessMock(), CommandManager.RegistrationEnvironment.DEDICATED);
        proxy.setTarget(target);

        // now change the target
        dispatcher = new CommandDispatcher<>();
        target = new DirectCommandRegister<>(dispatcher, new CommandRegistryAccessMock(), CommandManager.RegistrationEnvironment.DEDICATED);
        proxy.setTarget(target);

        // verify the consumer was unmodified, because the registered commands were flushed by the first setTarget()
        consumer.assertUnmodified();
    }

    @Test
    void setTarget_flushRefs_directFlushed() {
        var proxy = new DeferredProxyCommandRegister<String>(false);

        var dispatcher = new CommandDispatcher<String>();
        var target = new DirectCommandRegister<>(dispatcher, new CommandRegistryAccessMock(), CommandManager.RegistrationEnvironment.DEDICATED);
        proxy.setTarget(target);

        var consumer = new TestConsumer();
        proxy.register(LiteralArgumentBuilder.literal("test"), consumer);

        // now change the target
        dispatcher = new CommandDispatcher<>();
        target = new DirectCommandRegister<>(dispatcher, new CommandRegistryAccessMock(), CommandManager.RegistrationEnvironment.DEDICATED);
        proxy.setTarget(target);

        // verify the consumer was unmodified, because the registered commands were flushed by the first setTarget()
        consumer.assertUnmodified();
    }

    @Test
    void setTarget_keepRefs_deferredKept() {
        var proxy = new DeferredProxyCommandRegister<String>(true);

        var consumer = new TestConsumer();
        proxy.register(LiteralArgumentBuilder.literal("test"), consumer);

        var dispatcher = new CommandDispatcher<String>();
        var target = new DirectCommandRegister<>(dispatcher, new CommandRegistryAccessMock(), CommandManager.RegistrationEnvironment.DEDICATED);
        proxy.setTarget(target);

        // now change the target
        dispatcher = new CommandDispatcher<>();
        target = new DirectCommandRegister<>(dispatcher, new CommandRegistryAccessMock(), CommandManager.RegistrationEnvironment.DEDICATED);
        proxy.setTarget(target);

        // verify the consumer was updated
        consumer.assertUpdated();
    }

    @Test
    void setTarget_keepRefs_directKept() {
        var proxy = new DeferredProxyCommandRegister<String>(true);

        var dispatcher = new CommandDispatcher<String>();
        var target = new DirectCommandRegister<>(dispatcher, new CommandRegistryAccessMock(), CommandManager.RegistrationEnvironment.DEDICATED);
        proxy.setTarget(target);

        var consumer = new TestConsumer();
        proxy.register(LiteralArgumentBuilder.literal("test"), consumer);

        // now change the target
        dispatcher = new CommandDispatcher<>();
        target = new DirectCommandRegister<>(dispatcher, new CommandRegistryAccessMock(), CommandManager.RegistrationEnvironment.DEDICATED);
        proxy.setTarget(target);

        // verify the consumer was updated
        consumer.assertUpdated();
    }

    private static class TestConsumer implements CommandConsumer<String> {

        private LiteralCommandNode<String> command, oldCommand;

        @Override
        public void acceptCommand(LiteralCommandNode<String> command) {
            if (this.command != null) {
                this.oldCommand = this.command;
            }

            this.command = command;
        }

        public void assertEmpty() {
            assertNull(command, () -> "The reference was expected to be empty, but is referencing %s".formatted(command));
        }

        public void assertEqualsCommand(CommandDispatcher<String> dispatcher, String name) {
            var ref = dispatcher.getRoot().getChild(name);
            assertNotNull(ref);
            assertEquals(ref, command, "Command is not registered on the dispatcher");
        }

        public void assertUpdated() {
            assertNotNull(oldCommand, "Reference wasn't modified but was expected to be updated");
        }

        public void assertUnmodified() {
            assertNull(oldCommand, "Reference was updated but was expected to be unmodified");
        }
    }
}