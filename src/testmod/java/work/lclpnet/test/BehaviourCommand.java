package work.lclpnet.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import work.lclpnet.kibu.behaviour.world.ServerWorldBehaviour;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class BehaviourCommand {

    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(command());
    }

    private LiteralArgumentBuilder<CommandSourceStack> command() {
        return literal("kibu:behaviour")
                .requires(s -> s.hasPermission(2))
                .then(literal("fluid_ticks")
                        .then(argument("enabled", BoolArgumentType.bool())
                                .executes(this::modifyFluidTicks)));
    }

    private int modifyFluidTicks(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        boolean enabled = BoolArgumentType.getBool(ctx, "enabled");
        ServerPlayer player = ctx.getSource().getPlayerOrException();

        ServerWorldBehaviour.setFluidTicksEnabled(player.level(), enabled);

        player.sendSystemMessage(Component.literal("Set behaviour fluid_ticks to \"" + enabled + "\""));

        return 1;
    }
}
