package work.lclpnet.kibu.cmd;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.command.ServerCommandSource;
import work.lclpnet.kibu.cmd.type.CommandFactory;
import work.lclpnet.kibu.cmd.util.DeferredProxyCommandRegister;
import work.lclpnet.kibu.cmd.util.MinecraftCommandRegister;

import java.util.concurrent.CompletableFuture;

public class KibuCommands implements ModInitializer {

    static final DeferredProxyCommandRegister<ServerCommandSource> PROXY = new DeferredProxyCommandRegister<>();

    @Override
    public void onInitialize() {
        var register = new MinecraftCommandRegister();
        PROXY.setTarget(register);

        register.init();
    }

    public static CompletableFuture<LiteralCommandNode<ServerCommandSource>> register(LiteralArgumentBuilder<ServerCommandSource> command) {
        return PROXY.register(command);
    }

    public static CompletableFuture<LiteralCommandNode<ServerCommandSource>> register(CommandFactory<ServerCommandSource> factory) {
        return PROXY.register(factory);
    }

    public static boolean unregister(LiteralCommandNode<ServerCommandSource> command) {
        return PROXY.unregister(command);
    }
}
