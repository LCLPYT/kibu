package work.lclpnet.kibu.cmd.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import work.lclpnet.kibu.cmd.KibuCommands;
import work.lclpnet.kibu.cmd.type.RedirectAware;

import java.util.ArrayList;

public class CommandDispatcherUtils {

    private CommandDispatcherUtils() {}

    public static <S> LiteralCommandNode<S> register(CommandDispatcher<S> dispatcher, LiteralArgumentBuilder<S> command) {
        return dispatcher.register(command);
    }

    public static <S> void unregister(CommandDispatcher<S> dispatcher, LiteralCommandNode<S> command) {
        var root = dispatcher.getRoot();

        var dependants = new ArrayList<LiteralCommandNode<ServerCommandSource>>();

        try {
            var children = CommandInternals.getChildren(root);
            children.remove(command.getName());

            var literals = CommandInternals.getLiterals(root);
            literals.remove(command.getName());

            // removing redirects will only work in mixin context
            if (root instanceof RedirectAware redirectAware) {
                var redirectsMap = redirectAware.kibu$getRedirects();
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
                            LiteralCommandNode<ServerCommandSource> literal = (LiteralCommandNode<ServerCommandSource>) commandNode;

                            dependants.add(literal);
                        }
                    });

                    redirectsMap.remove(command);
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to unregister command", e);
        }

        // unregister dependants as well, as functionality is broken
        dependants.forEach(KibuCommands::unregister);
    }
}
