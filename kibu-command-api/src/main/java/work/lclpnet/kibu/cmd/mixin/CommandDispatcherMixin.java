package work.lclpnet.kibu.cmd.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import work.lclpnet.kibu.cmd.util.RedirectAware;

import java.util.ArrayList;

@Mixin(CommandDispatcher.class)
public class CommandDispatcherMixin {

    @Shadow(remap = false) @Final
    private RootCommandNode<?> root;

    @Inject(
            method = "register",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/brigadier/tree/RootCommandNode;addChild(Lcom/mojang/brigadier/tree/CommandNode;)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            remap = false
    )
    public <S> void beforeRegister(LiteralArgumentBuilder<S> builder, CallbackInfoReturnable<LiteralCommandNode<S>> cir, LiteralCommandNode<S> command) {
        var redirect = command.getRedirect();
        if (!(redirect instanceof LiteralCommandNode<S> literal)) return;

        var redirects = ((RedirectAware) root).kibu$getRedirects();

        // track all redirects on the root node
        redirects.computeIfAbsent(literal, l -> new ArrayList<>()).add(command);
    }
}
