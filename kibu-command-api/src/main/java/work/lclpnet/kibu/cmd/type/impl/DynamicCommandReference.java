package work.lclpnet.kibu.cmd.type.impl;

import com.mojang.brigadier.tree.LiteralCommandNode;
import work.lclpnet.kibu.cmd.type.CommandConsumer;
import work.lclpnet.kibu.cmd.type.CommandReference;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DynamicCommandReference<S> implements CommandReference<S>, CommandConsumer<S> {

    private final Consumer<LiteralCommandNode<S>> unregisterAction;
    private LiteralCommandNode<S> command = null;
    private volatile CompletableFuture<CommandReference<S>> readyFuture = null;

    public DynamicCommandReference(Consumer<LiteralCommandNode<S>> unregisterAction) {
        this.unregisterAction = unregisterAction;
    }

    @Override
    public Optional<LiteralCommandNode<S>> getCommand() {
        return Optional.ofNullable(command);
    }

    @Override
    public CompletableFuture<CommandReference<S>> whenReady() {
        if (readyFuture != null) {
            return readyFuture;
        }

        synchronized (this) {
            if (readyFuture == null) {
                readyFuture = new CompletableFuture<>();

                if (command != null) {
                    readyFuture.complete(this);
                }
            }
        }

        return readyFuture;
    }

    @Override
    public void unregister() {
        getCommand().ifPresent(unregisterAction);
    }

    @Override
    public void acceptCommand(LiteralCommandNode<S> command) {
        synchronized (this) {
            this.command = command;

            if (readyFuture != null) {
                readyFuture.complete(this);
            }
        }
    }
}
