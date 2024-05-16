package work.lclpnet.kibu.access.mixin;

import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TeamS2CPacket.SerializableTeam.class)
public interface SerializableTeamAccessor {

    @Accessor
    @Mutable
    void setDisplayName(Text displayName);

    @Accessor
    @Mutable
    void setPrefix(Text prefix);

    @Accessor
    @Mutable
    void setSuffix(Text suffix);

    @Accessor
    @Mutable
    void setNameTagVisibilityRule(String nameTagVisibilityRule);

    @Accessor
    @Mutable
    void setCollisionRule(String collisionRule);

    @Accessor
    @Mutable
    void setColor(Formatting color);

    @Accessor
    @Mutable
    void setFriendlyFlags(int friendlyFlags);
}
