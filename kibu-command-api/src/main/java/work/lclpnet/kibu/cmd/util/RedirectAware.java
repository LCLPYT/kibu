package work.lclpnet.kibu.cmd.util;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public interface RedirectAware {

    @Nonnull
    Map<LiteralCommandNode<?>, List<CommandNode<?>>> kibu$getRedirects();
}
