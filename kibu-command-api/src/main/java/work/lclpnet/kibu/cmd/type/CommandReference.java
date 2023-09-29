package work.lclpnet.kibu.cmd.type;

import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * A reference to a {@link LiteralCommandNode}.
 * The referenced command may change and can be null.
 * @param <S>
 */
public interface CommandReference<S> {

    /**
     * Get the referenced command.
     * May change during execution.
     * @return The optional command.
     */
    Optional<LiteralCommandNode<S>> getCommand();

    /**
     * Get a future that completes when the command reference first has an entry.
     * @return A complet
     */
    CompletableFuture<CommandReference<S>> whenReady();

    void unregister();
}
