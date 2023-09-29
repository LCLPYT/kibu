package work.lclpnet.kibu.cmd.util;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import work.lclpnet.kibu.cmd.type.CommandConsumer;
import work.lclpnet.kibu.cmd.type.CommandFactory;
import work.lclpnet.kibu.cmd.type.CommandRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class DeferredProxyCommandRegister<S> implements CommandRegister<S> {

    private final List<DeferredRegisterItem<S, LiteralArgumentBuilder<S>>> deferred = new ArrayList<>();
    private final List<DeferredRegisterItem<S, CommandFactory<S>>> deferredFactories = new ArrayList<>();
    private final Object mutex = new Object();
    private final boolean keepReferences;
    private CommandRegister<S> target = null;

    public DeferredProxyCommandRegister() {
        this(false);
    }

    public DeferredProxyCommandRegister(boolean keepReferences) {
        this.keepReferences = keepReferences;
    }

    public void setTarget(CommandRegister<S> target) {
        synchronized (mutex) {
            this.target = target;
            registerDeferred();
        }
    }

    private void registerDeferred() {
        deferred.forEach(defer -> target.register(defer.item(), defer.consumer()));
        deferredFactories.forEach(defer -> target.register(defer.item(), defer.consumer()));

        if (!keepReferences) {
            deferred.clear();
            deferredFactories.clear();
        }
    }

    @Override
    public boolean register(LiteralArgumentBuilder<S> command, CommandConsumer<S> consumer) {
        return register(command, consumer, deferred, (cmd, cmdConsumer) -> target.register(cmd, cmdConsumer));
    }

    @Override
    public boolean register(CommandFactory<S> factory, CommandConsumer<S> consumer) {
        return register(factory, consumer, deferredFactories, (fac, cmdConsumer) -> target.register(fac, cmdConsumer));
    }

    @Override
    public boolean unregister(LiteralCommandNode<S> command) {
        if (target == null) return false;  // cannot unregister, nothing happens

        target.unregister(command);

        return true;
    }

    private <T> boolean register(T obj, CommandConsumer<S> reference, List<DeferredRegisterItem<S, T>> list, BiConsumer<T, CommandConsumer<S>> proxy) {
        synchronized (mutex) {
            boolean noTarget = target == null;

            if (noTarget || keepReferences) {
                list.add(new DeferredRegisterItem<>(obj, reference));

                if (noTarget) {
                    return false;
                }
            }
        }

        proxy.accept(obj, reference);

        return true;
    }

    private record DeferredRegisterItem<S, T>(T item, CommandConsumer<S> consumer) {}
}
