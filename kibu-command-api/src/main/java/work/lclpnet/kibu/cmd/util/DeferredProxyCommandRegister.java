package work.lclpnet.kibu.cmd.util;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import work.lclpnet.kibu.cmd.type.CommandRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DeferredProxyCommandRegister<S> implements CommandRegister<S> {

    private final List<DeferredRegisterItem<S>> deferred = new ArrayList<>();
    private final Object mutex = new Object();
    private CommandRegister<S> target = null;

    public void setTarget(CommandRegister<S> target) {
        synchronized (mutex) {
            boolean init = this.target == null && target != null;

            this.target = target;

            if (init) {
                registerDeferred();
            }
        }
    }

    private void registerDeferred() {
        deferred.forEach(defer -> {
            final var future = defer.future();
            proxyRegister(defer.command()).thenAccept(future::complete);
        });

        deferred.clear();
    }

    @Override
    public CompletableFuture<LiteralCommandNode<S>> register(LiteralArgumentBuilder<S> command) {
        synchronized (mutex) {
            if (target == null) {
                var future = new CompletableFuture<LiteralCommandNode<S>>();
                deferred.add(new DeferredRegisterItem<>(command, future));
                return future;
            }
        }

        return proxyRegister(command);
    }

    protected CompletableFuture<LiteralCommandNode<S>> proxyRegister(LiteralArgumentBuilder<S> command) {
        return target.register(command);
    }

    @Override
    public boolean unregister(LiteralCommandNode<S> command) {
        if (target == null) return false;  // cannot unregister, nothing happens

        target.unregister(command);

        return true;
    }

    private record DeferredRegisterItem<S>(
            LiteralArgumentBuilder<S> command,
            CompletableFuture<LiteralCommandNode<S>> future
    ) {}
}
