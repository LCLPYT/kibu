package work.lclpnet.kibu.cmd.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import work.lclpnet.kibu.cmd.type.CommandConsumer;
import work.lclpnet.kibu.cmd.type.CommandFactory;
import work.lclpnet.kibu.cmd.type.CommandRegister;
import work.lclpnet.kibu.cmd.type.CommandRegistrationContext;

import java.util.Objects;

public class DirectCommandRegister<S> implements CommandRegister<S>, CommandRegistrationContext {

    private final CommandDispatcher<S> dispatcher;
    private final CommandRegistryAccess registryAccess;
    private final CommandManager.RegistrationEnvironment environment;

    public DirectCommandRegister(CommandDispatcher<S> dispatcher, CommandRegistryAccess registryAccess,
                                 CommandManager.RegistrationEnvironment environment) {
        this.dispatcher = Objects.requireNonNull(dispatcher);
        this.registryAccess = Objects.requireNonNull(registryAccess);
        this.environment = Objects.requireNonNull(environment);
    }

    @Override
    public boolean register(LiteralArgumentBuilder<S> command, CommandConsumer<S> consumer) {
        var cmd = CommandDispatcherUtils.register(dispatcher, command);
        consumer.acceptCommand(cmd);
        return true;
    }

    @Override
    public boolean register(CommandFactory<S> factory, CommandConsumer<S> consumer) {
        LiteralArgumentBuilder<S> builder = factory.create(this);
        register(builder, consumer);
        return true;
    }

    @Override
    public boolean unregister(LiteralCommandNode<S> command) {
        CommandDispatcherUtils.unregister(dispatcher, command);
        return true;
    }

    @Override
    public CommandRegistryAccess registryAccess() {
        return registryAccess;
    }

    @Override
    public CommandManager.RegistrationEnvironment environment() {
        return environment;
    }
}
