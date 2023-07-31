package work.lclpnet.kibu.cmd.util;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import work.lclpnet.kibu.cmd.type.CommandFactory;
import work.lclpnet.kibu.cmd.type.CommandRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class DeferredProxyCommandRegister<S> implements CommandRegister<S> {

    private final List<DeferredRegisterItem<S, LiteralArgumentBuilder<S>>> deferred = new ArrayList<>();
    private final List<DeferredRegisterItem<S, CommandFactory<S>>> deferredFactories = new ArrayList<>();
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
            proxyRegister(defer.item()).thenAccept(future::complete);
        });

        deferred.clear();

        deferredFactories.forEach(defer -> {
            final var future = defer.future();
            proxyRegister(defer.item()).thenAccept(future::complete);
        });

        deferredFactories.clear();
    }

    @Override
    public CompletableFuture<LiteralCommandNode<S>> register(LiteralArgumentBuilder<S> command) {
        return register(command, deferred, this::proxyRegister);
    }

    @Override
    public CompletableFuture<LiteralCommandNode<S>> register(CommandFactory<S> factory) {
        return register(factory, deferredFactories, this::proxyRegister);
    }

    private <T> CompletableFuture<LiteralCommandNode<S>> register(T obj, List<DeferredRegisterItem<S, T>> list, Function<T, CompletableFuture<LiteralCommandNode<S>>> proxyRegister) {
        synchronized (mutex) {
            if (target == null) {
                var future = new CompletableFuture<LiteralCommandNode<S>>();
                list.add(new DeferredRegisterItem<>(obj, future));
                return future;
            }
        }

        return proxyRegister.apply(obj);
    }

    protected CompletableFuture<LiteralCommandNode<S>> proxyRegister(LiteralArgumentBuilder<S> command) {
        return target.register(command);
    }

    protected CompletableFuture<LiteralCommandNode<S>> proxyRegister(CommandFactory<S> factory) {
        return target.register(factory);
    }

    @Override
    public boolean unregister(LiteralCommandNode<S> command) {
        if (target == null) return false;  // cannot unregister, nothing happens

        target.unregister(command);

        return true;
    }

    private record DeferredRegisterItem<S, T>(T item, CompletableFuture<LiteralCommandNode<S>> future) {}
}
