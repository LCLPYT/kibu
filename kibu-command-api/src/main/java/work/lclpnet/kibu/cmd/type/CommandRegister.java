package work.lclpnet.kibu.cmd.type;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.concurrent.CompletableFuture;

public interface CommandRegister<S> {

    CompletableFuture<LiteralCommandNode<S>> register(LiteralArgumentBuilder<S> command);

    CompletableFuture<LiteralCommandNode<S>> register(CommandFactory<S> factory);

    boolean unregister(LiteralCommandNode<S> command);
}
