package work.lclpnet.kibu.cmd.type;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface RedirectAware {

    @NotNull
    Map<LiteralCommandNode<?>, List<CommandNode<?>>> kibu$getRedirects();
}
