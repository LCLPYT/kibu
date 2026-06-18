package work.lclpnet.kibu.access.network.packet;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket.Parameters;
import net.minecraft.world.scores.Team;
import net.minecraft.world.scores.TeamColor;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.access.mixin.ClientboundSetPlayerTeamAccessor;

import java.util.Optional;
import java.util.function.UnaryOperator;

public class TeamS2CPacketAccess {

    private TeamS2CPacketAccess() {}

    public static ClientboundSetPlayerTeamPacket modifyTeam(ClientboundSetPlayerTeamPacket packet, UnaryOperator<Parameters> team) {
        return withTeam(packet, packet.getParameters().map(team).orElse(null));
    }

    // return value is currently the identity, this maybe has to return another instance in the future
    public static ClientboundSetPlayerTeamPacket withTeam(ClientboundSetPlayerTeamPacket packet, @Nullable Parameters team) {
        ((ClientboundSetPlayerTeamAccessor) packet).setParameters(Optional.ofNullable(team));
        return packet;
    }

    public static Parameters withDisplayName(Parameters team, Component displayName) {
        return new Parameters(displayName, team.playerPrefix(), team.playerSuffix(), team.nameTagVisibility(),
                team.collisionRule(), team.color(), team.options());
    }

    public static Parameters withPrefix(Parameters team, Component prefix) {
        return new Parameters(team.displayName(), prefix, team.playerSuffix(), team.nameTagVisibility(),
                team.collisionRule(), team.color(), team.options());
    }

    public static Parameters withSuffix(Parameters team, Component suffix) {
        return new Parameters(team.displayName(), team.playerPrefix(), suffix, team.nameTagVisibility(),
                team.collisionRule(), team.color(), team.options());
    }

    public static Parameters withNameTagVisibilityRule(Parameters team, Team.Visibility rule) {
        return new Parameters(team.displayName(), team.playerPrefix(), team.playerSuffix(), rule,
                team.collisionRule(), team.color(), team.options());
    }

    public static Parameters withCollisionRule(Parameters team, Team.CollisionRule rule) {
        return new Parameters(team.displayName(), team.playerPrefix(), team.playerSuffix(), team.nameTagVisibility(),
                rule, team.color(), team.options());
    }

    public static Parameters withColor(Parameters team, @Nullable TeamColor color) {
        return new Parameters(team.displayName(), team.playerPrefix(), team.playerSuffix(), team.nameTagVisibility(),
                team.collisionRule(), Optional.ofNullable(color), team.options());
    }

    public static Parameters withFriendlyFlags(Parameters team, int friendlyFlags) {
        return new Parameters(team.displayName(), team.playerPrefix(), team.playerSuffix(), team.nameTagVisibility(),
                team.collisionRule(), team.color(), (byte) friendlyFlags);
    }

    public static Parameters withFriendlyFire(Parameters team, boolean friendlyFire) {
        int flags = team.options();

        if (friendlyFire) {
            flags |= 0b1;
        } else {
            flags &= ~0b1;
        }

        return withFriendlyFlags(team, flags);
    }

    public static Parameters withShowFriendlyInvisibles(Parameters team, boolean showFriendlyInvisibles) {
        int flags = team.options();

        if (showFriendlyInvisibles) {
            flags |= 0b10;
        } else {
            flags &= ~0b10;
        }

        return withFriendlyFlags(team, flags);
    }
}
