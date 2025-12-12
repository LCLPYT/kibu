package work.lclpnet.kibu.access.network.packet;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.access.mixin.ClientboundSetPlayerTeamAccessor;
import work.lclpnet.kibu.access.mixin.ClientboundSetPlayerTeamPacket$ParametersAccessor;

import java.util.Optional;
import java.util.function.UnaryOperator;

public class TeamS2CPacketAccess {

    private TeamS2CPacketAccess() {}

    public static ClientboundSetPlayerTeamPacket modifyTeam(ClientboundSetPlayerTeamPacket packet, UnaryOperator<ClientboundSetPlayerTeamPacket.Parameters> team) {
        return withTeam(packet, packet.getParameters().map(team).orElse(null));
    }

    // return value is currently the identity, this maybe has to return another instance in the future
    public static ClientboundSetPlayerTeamPacket withTeam(ClientboundSetPlayerTeamPacket packet, @Nullable ClientboundSetPlayerTeamPacket.Parameters team) {
        ((ClientboundSetPlayerTeamAccessor) packet).setParameters(Optional.ofNullable(team));
        return packet;
    }

    public static ClientboundSetPlayerTeamPacket.Parameters withDisplayName(ClientboundSetPlayerTeamPacket.Parameters team, Component displayName) {
        ((ClientboundSetPlayerTeamPacket$ParametersAccessor) team).setDisplayName(displayName);
        return team;
    }

    public static ClientboundSetPlayerTeamPacket.Parameters withPrefix(ClientboundSetPlayerTeamPacket.Parameters team, Component prefix) {
        ((ClientboundSetPlayerTeamPacket$ParametersAccessor) team).setPlayerPrefix(prefix);
        return team;
    }

    public static ClientboundSetPlayerTeamPacket.Parameters withSuffix(ClientboundSetPlayerTeamPacket.Parameters team, Component suffix) {
        ((ClientboundSetPlayerTeamPacket$ParametersAccessor) team).setPlayerSuffix(suffix);
        return team;
    }

    public static ClientboundSetPlayerTeamPacket.Parameters withNameTagVisibilityRule(ClientboundSetPlayerTeamPacket.Parameters team, Team.Visibility rule) {
        ((ClientboundSetPlayerTeamPacket$ParametersAccessor) team).setNametagVisibility(rule);
        return team;
    }

    public static ClientboundSetPlayerTeamPacket.Parameters withCollisionRule(ClientboundSetPlayerTeamPacket.Parameters team, Team.CollisionRule rule) {
        ((ClientboundSetPlayerTeamPacket$ParametersAccessor) team).setCollisionRule(rule);
        return team;
    }

    public static ClientboundSetPlayerTeamPacket.Parameters withColor(ClientboundSetPlayerTeamPacket.Parameters team, ChatFormatting color) {
        ((ClientboundSetPlayerTeamPacket$ParametersAccessor) team).setColor(color);
        return team;
    }

    public static ClientboundSetPlayerTeamPacket.Parameters withFriendlyFlags(ClientboundSetPlayerTeamPacket.Parameters team, int friendlyFlags) {
        ((ClientboundSetPlayerTeamPacket$ParametersAccessor) team).setOptions(friendlyFlags);
        return team;
    }

    public static ClientboundSetPlayerTeamPacket.Parameters withFriendlyFire(ClientboundSetPlayerTeamPacket.Parameters team, boolean friendlyFire) {
        int flags = team.getOptions();

        if (friendlyFire) {
            flags |= 0b1;
        } else {
            flags &= ~0b1;
        }

        return withFriendlyFlags(team, flags);
    }

    public static ClientboundSetPlayerTeamPacket.Parameters withShowFriendlyInvisibles(ClientboundSetPlayerTeamPacket.Parameters team, boolean showFriendlyInvisibles) {
        int flags = team.getOptions();

        if (showFriendlyInvisibles) {
            flags |= 0b10;
        } else {
            flags &= ~0b10;
        }

        return withFriendlyFlags(team, flags);
    }
}
