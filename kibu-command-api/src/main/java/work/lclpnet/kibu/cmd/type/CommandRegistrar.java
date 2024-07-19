package work.lclpnet.kibu.cmd.type;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;

public interface CommandRegistrar {

    CommandReference<ServerCommandSource> registerCommand(LiteralArgumentBuilder<ServerCommandSource> command);

    CommandReference<ServerCommandSource> registerCommand(CommandFactory<ServerCommandSource> factory);

    void unregisterCommand(LiteralCommandNode<ServerCommandSource> command);
}
