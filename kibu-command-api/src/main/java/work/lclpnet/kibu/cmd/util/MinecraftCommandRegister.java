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

public class MinecraftCommandRegister implements CommandRegister<ServerCommandSource>, Initializable {

    private MinecraftServer server;

    @Override
    public void init() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> this.server = server);
    }

    @Override
    public LiteralCommandNode<ServerCommandSource> register(LiteralArgumentBuilder<ServerCommandSource> command) {
        var registered = CommandDispatcherUtils.register(getDispatcher(), command);

        syncCommandTree();

        return registered;
    }

    @Override
    public void unregister(LiteralCommandNode<ServerCommandSource> command) {
        CommandDispatcherUtils.unregister(getDispatcher(), command);

        syncCommandTree();
    }

    private CommandDispatcher<ServerCommandSource> getDispatcher() {
        return this.server.getCommandManager().getDispatcher();
    }

    private void syncCommandTree() {
        var commandManager = server.getCommandManager();

        PlayerLookup.all(server).forEach(commandManager::sendCommandTree);
    }
}
