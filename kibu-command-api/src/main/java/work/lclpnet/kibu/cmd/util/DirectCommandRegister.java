package work.lclpnet.kibu.cmd.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import work.lclpnet.kibu.cmd.type.CommandRegister;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class DirectCommandRegister<S> implements CommandRegister<S> {

    private final CommandDispatcher<S> dispatcher;

    public DirectCommandRegister(CommandDispatcher<S> dispatcher) {
        this.dispatcher = Objects.requireNonNull(dispatcher);
    }

    @Override
    public CompletableFuture<LiteralCommandNode<S>> register(LiteralArgumentBuilder<S> command) {
        var cmd = CommandDispatcherUtils.register(dispatcher, command);
        return CompletableFuture.completedFuture(cmd);
    }

    @Override
    public boolean unregister(LiteralCommandNode<S> command) {
        CommandDispatcherUtils.unregister(dispatcher, command);
        return true;
    }
}
