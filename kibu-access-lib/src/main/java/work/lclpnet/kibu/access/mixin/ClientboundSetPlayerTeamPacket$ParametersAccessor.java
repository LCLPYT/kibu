package work.lclpnet.kibu.access.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.world.scores.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientboundSetPlayerTeamPacket.Parameters.class)
public interface ClientboundSetPlayerTeamPacket$ParametersAccessor {

    @Accessor
    @Mutable
    void setDisplayName(Component displayName);

    @Accessor
    @Mutable
    void setPlayerPrefix(Component prefix);

    @Accessor
    @Mutable
    void setPlayerSuffix(Component suffix);

    @Accessor
    @Mutable
    void setNametagVisibility(Team.Visibility nameTagVisibilityRule);

    @Accessor
    @Mutable
    void setCollisionRule(Team.CollisionRule collisionRule);

    @Accessor
    @Mutable
    void setColor(ChatFormatting color);

    @Accessor
    @Mutable
    void setOptions(int friendlyFlags);
}
