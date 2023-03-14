package work.lclpnet.kibu.cmd.mixin;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import work.lclpnet.kibu.cmd.util.RedirectAware;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(RootCommandNode.class)
public class RootCommandNodeMixin implements RedirectAware {

    @Unique
    private final Map<LiteralCommandNode<?>, List<CommandNode<?>>> redirects = new HashMap<>();

    @Nonnull
    @Override
    public Map<LiteralCommandNode<?>, List<CommandNode<?>>> kibu$getRedirects() {
        return redirects;
    }
}
