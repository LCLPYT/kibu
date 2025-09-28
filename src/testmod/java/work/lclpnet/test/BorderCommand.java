package work.lclpnet.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.packet.s2c.play.WorldBorderWarningBlocksChangedS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.access.network.packet.WorldBorderWarningBlocksChangedS2CPacketAccess;

public class BorderCommand {

    private boolean warning = false;

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(command());
    }

    private LiteralArgumentBuilder<ServerCommandSource> command() {
        return CommandManager.literal("kibu:border")
                .requires(s -> s.hasPermissionLevel(2))
                .executes(this::act);
    }

    private int act(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();

        warning = !warning;

        var packet = new WorldBorderWarningBlocksChangedS2CPacket(player.getEntityWorld().getWorldBorder());

        if (warning) {
            WorldBorderWarningBlocksChangedS2CPacketAccess.withWarningBlocks(packet, Integer.MAX_VALUE);
        }

        player.networkHandler.sendPacket(packet);

        return 1;
    }
}
