package work.lclpnet.kibu.cmd;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.cmd.util.DirectCommandRegister;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static org.junit.jupiter.api.Assertions.*;

public class KibuCommandsTest {

    @Test
    public void testRegisterWithoutBootstrap() {
        var dispatcher = new CommandDispatcher<ServerCommandSource>();
        var register = new DirectCommandRegister<>(dispatcher);

        KibuCommands.PROXY.setTarget(register);

        var cmd = KibuCommands.register(literal("test")).join();
        var registered = dispatcher.getRoot().getChild("test");

        assertNotNull(cmd);
        assertEquals(registered, cmd);
    }

    @Test
    public void testUnRegisterWithoutBootstrap() {
        var dispatcher = new CommandDispatcher<ServerCommandSource>();
        var register = new DirectCommandRegister<>(dispatcher);

        KibuCommands.PROXY.setTarget(register);

        var cmd = KibuCommands.register(literal("test")).join();
        var registered = dispatcher.getRoot().getChild("test");

        assertNotNull(cmd);
        assertEquals(registered, cmd);

        KibuCommands.unregister(cmd);

        registered = dispatcher.getRoot().getChild("test");
        assertNull(registered);
    }
}
