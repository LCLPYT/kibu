package work.lclpnet.kibu.access.network.packet;

import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.access.mixin.SerializableTeamAccessor;
import work.lclpnet.kibu.access.mixin.TeamS2CPacketAccessor;

import java.util.Optional;
import java.util.function.UnaryOperator;

public class TeamS2CPacketAccess {

    private TeamS2CPacketAccess() {}

    public static TeamS2CPacket modifyTeam(TeamS2CPacket packet, UnaryOperator<TeamS2CPacket.SerializableTeam> team) {
        return withTeam(packet, packet.getTeam().map(team).orElse(null));
    }

    // return value is currently the identity, this maybe has to return another instance in the future
    public static TeamS2CPacket withTeam(TeamS2CPacket packet, @Nullable TeamS2CPacket.SerializableTeam team) {
        ((TeamS2CPacketAccessor) packet).setTeam(Optional.ofNullable(team));
        return packet;
    }

    public static TeamS2CPacket.SerializableTeam withDisplayName(TeamS2CPacket.SerializableTeam team, Text displayName) {
        ((SerializableTeamAccessor) team).setDisplayName(displayName);
        return team;
    }

    public static TeamS2CPacket.SerializableTeam withPrefix(TeamS2CPacket.SerializableTeam team, Text prefix) {
        ((SerializableTeamAccessor) team).setPrefix(prefix);
        return team;
    }

    public static TeamS2CPacket.SerializableTeam withSuffix(TeamS2CPacket.SerializableTeam team, Text suffix) {
        ((SerializableTeamAccessor) team).setSuffix(suffix);
        return team;
    }

    public static TeamS2CPacket.SerializableTeam withNameTagVisibilityRule(TeamS2CPacket.SerializableTeam team, AbstractTeam.VisibilityRule rule) {
        ((SerializableTeamAccessor) team).setNameTagVisibilityRule(rule.name);
        return team;
    }

    public static TeamS2CPacket.SerializableTeam withCollisionRule(TeamS2CPacket.SerializableTeam team, AbstractTeam.CollisionRule rule) {
        ((SerializableTeamAccessor) team).setCollisionRule(rule.name);
        return team;
    }

    public static TeamS2CPacket.SerializableTeam withColor(TeamS2CPacket.SerializableTeam team, Formatting color) {
        ((SerializableTeamAccessor) team).setColor(color);
        return team;
    }

    public static TeamS2CPacket.SerializableTeam withFriendlyFlags(TeamS2CPacket.SerializableTeam team, int friendlyFlags) {
        ((SerializableTeamAccessor) team).setFriendlyFlags(friendlyFlags);
        return team;
    }

    public static TeamS2CPacket.SerializableTeam withFriendlyFire(TeamS2CPacket.SerializableTeam team, boolean friendlyFire) {
        int flags = team.getFriendlyFlagsBitwise();

        if (friendlyFire) {
            flags |= 0b1;
        } else {
            flags &= ~0b1;
        }

        return withFriendlyFlags(team, flags);
    }

    public static TeamS2CPacket.SerializableTeam withShowFriendlyInvisibles(TeamS2CPacket.SerializableTeam team, boolean showFriendlyInvisibles) {
        int flags = team.getFriendlyFlagsBitwise();

        if (showFriendlyInvisibles) {
            flags |= 0b10;
        } else {
            flags &= ~0b10;
        }

        return withFriendlyFlags(team, flags);
    }
}
