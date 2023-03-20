package work.lclpnet.kibu.cmd.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import work.lclpnet.kibu.cmd.type.Initializable;

import java.util.concurrent.CompletableFuture;

public class MinecraftCommandRegister extends DeferredProxyCommandRegister<ServerCommandSource> implements Initializable {

    private final Object mutex = new Object();
    private MinecraftServer server = null;
    private CommandDispatcher<ServerCommandSource> dispatcher = null;
    private boolean ready = false;

    @Override
    public void init() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            synchronized (mutex) {
                this.server = server;
            }

            this.update();
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            synchronized (mutex) {
                this.dispatcher = dispatcher;
            }

            this.update();
        });
    }

    private void update() {
        synchronized (mutex) {
            if (ready || dispatcher == null || server == null) return;

            ready = true;
            setTarget(new DirectCommandRegister<>(dispatcher));
        }
    }

    @Override
    protected CompletableFuture<LiteralCommandNode<ServerCommandSource>> proxyRegister(LiteralArgumentBuilder<ServerCommandSource> command) {
        var future = super.proxyRegister(command);

        syncCommandTree();

        return future;
    }

    @Override
    public boolean unregister(LiteralCommandNode<ServerCommandSource> command) {
        boolean unregistered = super.unregister(command);

        if (unregistered) {
            syncCommandTree();
        }

        return unregistered;
    }

    private void syncCommandTree() {
        final MinecraftServer server;

        synchronized (mutex) {
            server = this.server;
        }

        var commandManager = server.getCommandManager();

        PlayerLookup.all(server).forEach(commandManager::sendCommandTree);
    }
}
