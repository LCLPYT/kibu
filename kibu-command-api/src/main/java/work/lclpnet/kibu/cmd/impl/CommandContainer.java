package work.lclpnet.kibu.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import work.lclpnet.kibu.cmd.KibuCommands;
import work.lclpnet.kibu.cmd.type.CommandFactory;
import work.lclpnet.kibu.cmd.type.CommandReference;
import work.lclpnet.kibu.cmd.type.CommandRegistrar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandContainer implements CommandRegistrar {

    private final Object mutex = new Object();
    private final List<CommandReference<ServerCommandSource>> commands = new ArrayList<>();

    @Override
    public CommandReference<ServerCommandSource> registerCommand(LiteralArgumentBuilder<ServerCommandSource> command) {
        return store(KibuCommands.register(command));
    }

    @Override
    public CommandReference<ServerCommandSource> registerCommand(CommandFactory<ServerCommandSource> factory) {
        return store(KibuCommands.register(factory));
    }

    private CommandReference<ServerCommandSource> store(CommandReference<ServerCommandSource> cmd) {
        synchronized (mutex) {
            commands.add(cmd);
        }

        return cmd;
    }

    public Optional<CommandReference<ServerCommandSource>> getReferenceTo(LiteralCommandNode<ServerCommandSource> command) {
        synchronized (mutex) {
            return commands.stream().filter(ref -> {
                var cmd = ref.getCommand();
                return cmd.isPresent() && cmd.get().equals(command);
            }).findAny();
        }
    }

    @Override
    public void unregisterCommand(LiteralCommandNode<ServerCommandSource> command) {
        var optRef = getReferenceTo(command);
        if (optRef.isEmpty()) return;

        var ref = optRef.get();

        synchronized (mutex) {
            ref.unregister();
            commands.remove(ref);
        }
    }

    public void unload() {
        synchronized (mutex) {
            commands.forEach(CommandReference::unregister);
            commands.clear();
        }
    }
}
