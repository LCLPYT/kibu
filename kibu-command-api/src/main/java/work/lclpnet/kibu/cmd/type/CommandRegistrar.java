package work.lclpnet.kibu.cmd.type;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;

public interface CommandRegistrar {

    CommandReference<CommandSourceStack> registerCommand(LiteralArgumentBuilder<CommandSourceStack> command);

    CommandReference<CommandSourceStack> registerCommand(CommandFactory<CommandSourceStack> factory);

    void unregisterCommand(LiteralCommandNode<CommandSourceStack> command);
}
