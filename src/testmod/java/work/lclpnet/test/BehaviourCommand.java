package work.lclpnet.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import work.lclpnet.kibu.behaviour.world.ServerWorldBehaviour;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class BehaviourCommand {

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(command());
    }

    private LiteralArgumentBuilder<ServerCommandSource> command() {
        return literal("kibu:behaviour")
                .requires(s -> s.hasPermissionLevel(2))
                .then(literal("fluid_ticks")
                        .then(argument("enabled", BoolArgumentType.bool())
                                .executes(this::modifyFluidTicks)));
    }

    private int modifyFluidTicks(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        boolean enabled = BoolArgumentType.getBool(ctx, "enabled");
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();

        ServerWorldBehaviour.setFluidTicksEnabled(player.getServerWorld(), enabled);

        player.sendMessage(Text.literal("Set behaviour fluid_ticks to \"" + enabled + "\""));

        return 1;
    }
}
