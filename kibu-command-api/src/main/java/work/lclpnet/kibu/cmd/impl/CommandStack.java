package work.lclpnet.kibu.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.NotNull;
import work.lclpnet.kibu.cmd.type.CommandFactory;
import work.lclpnet.kibu.cmd.type.CommandReference;
import work.lclpnet.kibu.cmd.type.CommandRegistrar;

import java.util.Stack;
import java.util.function.Supplier;

public class CommandStack implements CommandRegistrar {

    private final Supplier<CommandRegistrar> factory;
    private CommandRegistrar current = null;
    private Stack<CommandRegistrar> stack = null;

    public CommandStack(Supplier<CommandRegistrar> factory) {
        this.factory = factory;
    }

    public void push() {
        synchronized (this) {
            if (stack == null) {
                stack = new Stack<>();
            }

            if (current != null) {
                stack.push(current);
            }

            current = null;
        }
    }

    public void pop() {
        synchronized (this) {
            maybeUnload(current);

            if (stack == null || stack.isEmpty()) {
                current = null;
                return;
            }

            current = stack.pop();
        }
    }

    public void unload() {
        synchronized (this) {
            maybeUnload(current);

            if (stack != null) {
                while (!stack.isEmpty()) {
                    var element = stack.pop();
                    maybeUnload(element);
                }
            }

            current = null;
            stack = null;
        }
    }

    protected CommandRegistrar current() {
        synchronized (this) {
            if (current == null) {
                current = factory.get();
            }

            return current;
        }
    }

    @Override
    public CommandReference<CommandSourceStack> registerCommand(LiteralArgumentBuilder<CommandSourceStack> command) {
        return current().registerCommand(command);
    }

    @Override
    public CommandReference<CommandSourceStack> registerCommand(CommandFactory<CommandSourceStack> factory) {
        return current().registerCommand(factory);
    }

    @Override
    public void unregisterCommand(LiteralCommandNode<CommandSourceStack> command) {
        current().unregisterCommand(command);
    }

    private void maybeUnload(@NotNull CommandRegistrar registrar) {
        if (registrar instanceof CommandContainer container) {
            container.unload();
        }
    }
}
