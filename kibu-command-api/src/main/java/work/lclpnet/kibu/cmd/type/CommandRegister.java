package work.lclpnet.kibu.cmd.type;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

public interface CommandRegister<S> {

    LiteralCommandNode<S> register(LiteralArgumentBuilder<S> command);

    void unregister(LiteralCommandNode<S> command);
}
