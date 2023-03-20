package work.lclpnet.kibu.cmd;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.ModInitializer;
import work.lclpnet.kibu.cmd.type.CommandRegister;
import work.lclpnet.kibu.cmd.type.Initializable;
import work.lclpnet.kibu.cmd.util.MinecraftCommandRegister;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class KibuCommands<S> implements ModInitializer {

    private static final Object mutex = new Object();
    private static KibuCommands<?> instance = null;
    private final CommandRegister<S> register;

    @SuppressWarnings("unchecked")
    public KibuCommands() {
        this((CommandRegister<S>) new MinecraftCommandRegister());
    }

    public KibuCommands(CommandRegister<S> register) {
        this.register = register;
    }

    @Nonnull
    private static <S> KibuCommands<S> getInstance() {
        @SuppressWarnings("unchecked")
        KibuCommands<S> ret = (KibuCommands<S>) instance;
        if (ret == null) throw new IllegalStateException("Not initialized");

        return ret;
    }

    @Override
    public void onInitialize() {
        synchronized (mutex) {
            if (instance != null) throw new IllegalStateException("Already initialized");

            instance = this;
        }

        if (register instanceof Initializable initializable) {
            initializable.init();
        }
    }

    private CompletableFuture<LiteralCommandNode<S>> register0(LiteralArgumentBuilder<S> command) {
        return register.register(command);
    }

    private void unregister0(LiteralCommandNode<S> command) {
        register.unregister(command);
    }

    public static <S> CompletableFuture<LiteralCommandNode<S>> register(LiteralArgumentBuilder<S> command) {
        KibuCommands<S> genericInstance = getInstance();
        return genericInstance.register0(command);
    }

    public static <S> void unregister(LiteralCommandNode<S> command) {
        KibuCommands<S> genericInstance = getInstance();
        genericInstance.unregister0(command);
    }
}
