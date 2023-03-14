package work.lclpnet.kibu.cmd;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import work.lclpnet.kibu.cmd.util.RedirectAware;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class KibuCommands implements ModInitializer {

    private static final Object mutex = new Object();
    private static KibuCommands instance = null;
    private MinecraftServer server;

    @Nonnull
    private static KibuCommands getInstance() {
        KibuCommands ret = instance;
        if (ret == null) throw new IllegalStateException("Not initialized");
        return ret;
    }

    @Override
    public void onInitialize() {
        synchronized (mutex) {
            if (instance != null) throw new IllegalStateException("Already initialized");

            instance = this;
        }

        ServerLifecycleEvents.SERVER_STARTING.register(server -> this.server = server);
    }

    @SuppressWarnings("unused")
    public static LiteralCommandNode<ServerCommandSource> register(LiteralArgumentBuilder<ServerCommandSource> command) {
        var server = getInstance().server;
        var manager = server.getCommandManager();
        var dispatcher = manager.getDispatcher();

        var registered = dispatcher.register(command);

        syncCommandTree();

        return registered;
    }

    @SuppressWarnings("unused")
    public static <S> void unregister(LiteralCommandNode<S> command) {
        var server = getInstance().server;
        var root = server.getCommandManager().getDispatcher().getRoot();

        var dependants = new ArrayList<LiteralCommandNode<S>>();

        try {
            var children = CommandInternals.getChildren(root);
            children.remove(command.getName());

            var literals = CommandInternals.getLiterals(root);
            literals.remove(command.getName());

            var redirectsMap = ((RedirectAware) root).kibu$getRedirects();
            var redirects = redirectsMap.get(command);

            if (redirects != null) {
                redirects.forEach(commandNode -> {
                    // remove the redirect
                    try {
                        CommandInternals.setRedirect(commandNode, null);
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException("Failed to remove redirect", e);
                    }

                    // if the commandNode is a literal, add it for removal
                    if (commandNode instanceof LiteralCommandNode<?>) {
                        @SuppressWarnings("unchecked")
                        LiteralCommandNode<S> literal = (LiteralCommandNode<S>) commandNode;

                        dependants.add(literal);
                    }
                });

                redirectsMap.remove(command);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to unregister command", e);
        }

        // unregister dependants as well, as functionality is broken
        dependants.forEach(KibuCommands::unregister);

        syncCommandTree();
    }

    private static void syncCommandTree() {
        var server = getInstance().server;
        var commandManager = server.getCommandManager();

        PlayerLookup.all(server).forEach(commandManager::sendCommandTree);
    }
}
