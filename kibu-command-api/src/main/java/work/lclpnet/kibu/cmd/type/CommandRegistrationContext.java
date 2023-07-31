package work.lclpnet.kibu.cmd.type;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;

public interface CommandRegistrationContext {

    CommandRegistryAccess registryAccess();

    CommandManager.RegistrationEnvironment environment();
}
