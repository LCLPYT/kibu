package work.lclpnet.kibu.cmd.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import work.lclpnet.kibu.cmd.type.CommandFactory;
import work.lclpnet.kibu.cmd.type.CommandRegister;
import work.lclpnet.kibu.cmd.type.CommandRegistrationContext;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<LiteralCommandNode<S>> register(LiteralArgumentBuilder<S> command) {
        var cmd = CommandDispatcherUtils.register(dispatcher, command);
        return CompletableFuture.completedFuture(cmd);
    }

    @Override
    public CompletableFuture<LiteralCommandNode<S>> register(CommandFactory<S> factory) {
        LiteralArgumentBuilder<S> builder = factory.create(this);
        return register(builder);
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
