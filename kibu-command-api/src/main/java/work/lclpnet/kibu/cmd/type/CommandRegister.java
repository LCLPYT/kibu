package work.lclpnet.kibu.cmd.type;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

public interface CommandRegister<S> {

    boolean register(LiteralArgumentBuilder<S> command, CommandConsumer<S> consumer);

    boolean register(CommandFactory<S> factory, CommandConsumer<S> consumer);

    boolean unregister(LiteralCommandNode<S> command);
}
