package work.lclpnet.kibu.cmd.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import work.lclpnet.kibu.cmd.type.CommandRegister;
import work.lclpnet.kibu.cmd.type.Initializable;

import java.util.ArrayList;
import java.util.List;

public class MinecraftCommandRegister implements CommandRegister<ServerCommandSource>, Initializable {

    private final List<LiteralArgumentBuilder<ServerCommandSource>> deferred = new ArrayList<>();
    private final Object mutex = new Object();
    private MinecraftServer server = null;

    @Override
    public void init() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            synchronized (mutex) {
                this.server = server;
                loadDeferred();
            }
        });
    }

    @Override
    public LiteralCommandNode<ServerCommandSource> register(LiteralArgumentBuilder<ServerCommandSource> command) {
        if (command == null) throw new NullPointerException("Command cannot be null");

        synchronized (mutex) {
            if (server == null) {
                // server is not loaded yet, defer registration
                deferred.add(command);
            }
        }

        var registered = CommandDispatcherUtils.register(getDispatcher(), command);

        syncCommandTree();

        return registered;
    }

    @Override
    public void unregister(LiteralCommandNode<ServerCommandSource> command) {
        if (server == null) return;  // cannot register yet

        CommandDispatcherUtils.unregister(getDispatcher(), command);

        syncCommandTree();
    }

    private void loadDeferred() {
        this.deferred.forEach(this::register);
        this.deferred.clear();
    }

    private CommandDispatcher<ServerCommandSource> getDispatcher() {
        if (this.server == null) throw new IllegalStateException("Server is not initialized yet");
        return this.server.getCommandManager().getDispatcher();
    }

    private void syncCommandTree() {
        var commandManager = server.getCommandManager();

        PlayerLookup.all(server).forEach(commandManager::sendCommandTree);
    }
}
