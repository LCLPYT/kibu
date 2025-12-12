package work.lclpnet.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDistancePacket;
import net.minecraft.server.level.ServerPlayer;
import work.lclpnet.kibu.access.network.packet.WorldBorderWarningBlocksChangedS2CPacketAccess;

public class BorderCommand {

    private boolean warning = false;

    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(command());
    }

    private LiteralArgumentBuilder<CommandSourceStack> command() {
        return Commands.literal("kibu:border")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .executes(this::act);
    }

    private int act(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();

        warning = !warning;

        var packet = new ClientboundSetBorderWarningDistancePacket(player.level().getWorldBorder());

        if (warning) {
            WorldBorderWarningBlocksChangedS2CPacketAccess.withWarningBlocks(packet, Integer.MAX_VALUE);
        }

        player.connection.send(packet);

        return 1;
    }
}
