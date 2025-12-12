package work.lclpnet.kibu.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import work.lclpnet.kibu.cmd.KibuCommands;
import work.lclpnet.kibu.cmd.type.CommandFactory;
import work.lclpnet.kibu.cmd.type.CommandReference;
import work.lclpnet.kibu.cmd.type.CommandRegistrar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandContainer implements CommandRegistrar {

    private final Object mutex = new Object();
    private final List<CommandReference<CommandSourceStack>> commands = new ArrayList<>();

    @Override
    public CommandReference<CommandSourceStack> registerCommand(LiteralArgumentBuilder<CommandSourceStack> command) {
        return store(KibuCommands.register(command));
    }

    @Override
    public CommandReference<CommandSourceStack> registerCommand(CommandFactory<CommandSourceStack> factory) {
        return store(KibuCommands.register(factory));
    }

    private CommandReference<CommandSourceStack> store(CommandReference<CommandSourceStack> cmd) {
        synchronized (mutex) {
            commands.add(cmd);
        }

        return cmd;
    }

    public Optional<CommandReference<CommandSourceStack>> getReferenceTo(LiteralCommandNode<CommandSourceStack> command) {
        synchronized (mutex) {
            return commands.stream().filter(ref -> {
                var cmd = ref.getCommand();
                return cmd.isPresent() && cmd.get().equals(command);
            }).findAny();
        }
    }

    @Override
    public void unregisterCommand(LiteralCommandNode<CommandSourceStack> command) {
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
