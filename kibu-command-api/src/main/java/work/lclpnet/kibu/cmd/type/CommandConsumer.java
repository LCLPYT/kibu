package work.lclpnet.kibu.cmd.type;

import com.mojang.brigadier.tree.LiteralCommandNode;

public interface CommandConsumer<S> {

    void acceptCommand(LiteralCommandNode<S> command);
}
