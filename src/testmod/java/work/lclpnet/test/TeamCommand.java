package work.lclpnet.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.access.network.packet.TeamS2CPacketAccess;

public class TeamCommand {

    private boolean showInvisible = false;

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(command());
    }

    private LiteralArgumentBuilder<ServerCommandSource> command() {
        return CommandManager.literal("kibu:team")
                .requires(s -> s.hasPermissionLevel(2))
                .executes(this::act);
    }

    private int act(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();

        Scoreboard scoreboard = player.getWorld().getScoreboard();
        Team team = scoreboard.getTeam("kibu_test");

        if (team == null) {
            team = scoreboard.addTeam("kibu_test");
        }

        showInvisible = !showInvisible;

        var packet = TeamS2CPacketAccess.modifyTeam(TeamS2CPacket.updateTeam(team, false), serializable ->
                TeamS2CPacketAccess.withShowFriendlyInvisibles(serializable, showInvisible));

        player.networkHandler.sendPacket(packet);

        return 1;
    }
}
