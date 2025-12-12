package work.lclpnet.kibu.cmd.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import work.lclpnet.kibu.cmd.type.CommandConsumer;
import work.lclpnet.kibu.cmd.type.CommandFactory;
import work.lclpnet.kibu.cmd.type.CommandRegister;
import work.lclpnet.kibu.cmd.type.Initializable;

public class MinecraftCommandRegister implements CommandRegister<CommandSourceStack>, Initializable {

    private final Object mutex = new Object();
    private final DeferredProxyCommandRegister<CommandSourceStack> deferredRegister;
    private MinecraftServer server = null;
    private CommandDispatcher<CommandSourceStack> dispatcher = null;
    private CommandBuildContext registryAccess = null;
    private Commands.CommandSelection environment = null;
    private boolean ready = false;

    public MinecraftCommandRegister(boolean isClient) {
        this.deferredRegister = new DeferredProxyCommandRegister<>(isClient);
    }

    @Override
    public void init() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            synchronized (mutex) {
                this.server = server;
            }

            this.update();
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> this.reset());

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            synchronized (mutex) {
                this.dispatcher = dispatcher;
                this.registryAccess = registryAccess;
                this.environment = environment;
            }

            this.update();
        });
    }

    private void reset() {
        synchronized (this) {
            this.ready = false;
            this.server = null;
            this.registryAccess = null;
            this.environment = null;
        }
    }

    private void update() {
        synchronized (mutex) {
            if (ready || dispatcher == null || registryAccess == null || environment == null || server == null) return;

            ready = true;
            deferredRegister.setTarget(new DirectCommandRegister<>(dispatcher, registryAccess, environment));
        }

        syncCommandTree();
    }

    @Override
    public boolean register(LiteralArgumentBuilder<CommandSourceStack> command, CommandConsumer<CommandSourceStack> consumer) {
        if (deferredRegister.register(command, consumer)) {
            syncCommandTree();
            return true;
        }

        return false;
    }

    @Override
    public boolean register(CommandFactory<CommandSourceStack> factory, CommandConsumer<CommandSourceStack> consumer) {
        if (deferredRegister.register(factory, consumer)) {
            syncCommandTree();
            return true;
        }

        return false;
    }

    @Override
    public boolean unregister(LiteralCommandNode<CommandSourceStack> command) {
        if (deferredRegister.unregister(command)) {
            syncCommandTree();
            return true;
        }

        return false;
    }

    private void syncCommandTree() {
        final MinecraftServer server;

        synchronized (mutex) {
            server = this.server;
        }

        if (server == null) return;

        var commandManager = server.getCommands();

        PlayerLookup.all(server).forEach(commandManager::sendCommands);
    }
}
