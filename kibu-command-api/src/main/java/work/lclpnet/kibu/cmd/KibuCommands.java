package work.lclpnet.kibu.cmd;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import work.lclpnet.kibu.cmd.impl.DynamicCommandReference;
import work.lclpnet.kibu.cmd.type.CommandFactory;
import work.lclpnet.kibu.cmd.type.CommandReference;
import work.lclpnet.kibu.cmd.util.DeferredProxyCommandRegister;
import work.lclpnet.kibu.cmd.util.MinecraftCommandRegister;

public class KibuCommands implements ModInitializer {

    static final DeferredProxyCommandRegister<CommandSourceStack> PROXY = new DeferredProxyCommandRegister<>(false);

    @Override
    public void onInitialize() {
        boolean isClient = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;

        MinecraftCommandRegister register = new MinecraftCommandRegister(isClient);
        PROXY.setTarget(register);

        register.init();
    }

    public static CommandReference<CommandSourceStack> register(LiteralArgumentBuilder<CommandSourceStack> command) {
        var reference = new DynamicCommandReference<>(KibuCommands::unregister);
        PROXY.register(command, reference);
        return reference;
    }

    public static CommandReference<CommandSourceStack> register(CommandFactory<CommandSourceStack> factory) {
        var reference = new DynamicCommandReference<>(KibuCommands::unregister);
        PROXY.register(factory, reference);
        return reference;
    }

    public static boolean unregister(LiteralCommandNode<CommandSourceStack> command) {
        return PROXY.unregister(command);
    }
}
