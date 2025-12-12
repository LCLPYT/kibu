package work.lclpnet.kibu.cmd.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.Commands;
import work.lclpnet.kibu.cmd.type.CommandConsumer;
import work.lclpnet.kibu.cmd.type.CommandFactory;
import work.lclpnet.kibu.cmd.type.CommandRegister;
import work.lclpnet.kibu.cmd.type.CommandRegistrationContext;

import java.util.Objects;

public class DirectCommandRegister<S> implements CommandRegister<S>, CommandRegistrationContext {

    private final CommandDispatcher<S> dispatcher;
    private final CommandBuildContext registryAccess;
    private final Commands.CommandSelection environment;

    public DirectCommandRegister(CommandDispatcher<S> dispatcher, CommandBuildContext registryAccess,
                                 Commands.CommandSelection environment) {
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
    public CommandBuildContext registryAccess() {
        return registryAccess;
    }

    @Override
    public Commands.CommandSelection environment() {
        return environment;
    }
}
