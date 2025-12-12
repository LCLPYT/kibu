package work.lclpnet.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import work.lclpnet.kibu.access.network.packet.TeamS2CPacketAccess;

public class TeamCommand {

    private boolean showInvisible = false;

    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(command());
    }

    private LiteralArgumentBuilder<CommandSourceStack> command() {
        return Commands.literal("kibu:team")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .executes(this::act);
    }

    private int act(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();

        Scoreboard scoreboard = player.level().getScoreboard();
        PlayerTeam team = scoreboard.getPlayerTeam("kibu_test");

        if (team == null) {
            team = scoreboard.addPlayerTeam("kibu_test");
        }

        showInvisible = !showInvisible;

        var packet = TeamS2CPacketAccess.modifyTeam(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, false), serializable ->
                TeamS2CPacketAccess.withShowFriendlyInvisibles(serializable, showInvisible));

        player.connection.send(packet);

        return 1;
    }
}
