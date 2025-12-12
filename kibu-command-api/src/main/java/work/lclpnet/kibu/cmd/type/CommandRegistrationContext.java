package work.lclpnet.kibu.cmd.type;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.Commands;

public interface CommandRegistrationContext {

    CommandBuildContext registryAccess();

    Commands.CommandSelection environment();
}
