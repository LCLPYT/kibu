package work.lclpnet.kibu.cmd.util;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MinecraftCommandRegisterTest {


    @Test
    void testServerChangedBuilderRegistrationClient() {
        testServerChangedRegistrationClient(register -> register.register(CommandManager.literal("test")
                .executes(ctx -> 1), cmd -> {}));
    }

    @Test
    void testServerChangedFactoryRegistrationClient() {
        testServerChangedRegistrationClient(register -> register.register(registrationCtx -> CommandManager.literal("test")
                .executes(ctx -> 1), cmd -> {}));
    }

    /**
     * On integrated servers (single player), there can be multiple command registration events,
     * e.g. when joining another world.
     * Commands registered using a {@link MinecraftCommandRegister} should be registered in these sessions as well.
     */
    private static void testServerChangedRegistrationClient(Consumer<MinecraftCommandRegister> registerAction) {
        var register = new MinecraftCommandRegister(true);
        registerAction.accept(register);
        register.init();  // register event listeners

        var dispatcher = new CommandDispatcher<ServerCommandSource>();
        CommandRegistryAccessMock registryAccess = new CommandRegistryAccessMock();
        var environment = CommandManager.RegistrationEnvironment.INTEGRATED;

        CommandRegistrationCallback.EVENT.invoker().register(dispatcher, registryAccess, environment);

        MinecraftServer server = mock();
        when(server.getCommandManager()).thenReturn(mock());

        ServerLifecycleEvents.SERVER_STARTING.invoker().onServerStarting(server);

        // now the command should be registered

        assertCommandExists(dispatcher);

        // simulate server change
        ServerLifecycleEvents.SERVER_STOPPED.invoker().onServerStopped(server);

        dispatcher = new CommandDispatcher<>();

        CommandRegistrationCallback.EVENT.invoker().register(dispatcher, registryAccess, environment);
        ServerLifecycleEvents.SERVER_STARTING.invoker().onServerStarting(server);

        // verify the command is also registered on the new dispatcher
        assertCommandExists(dispatcher);
    }

    private static void assertCommandExists(CommandDispatcher<ServerCommandSource> dispatcher) {
        var ref = dispatcher.getRoot().getChild("test");
        assertNotNull(ref);
    }
}
