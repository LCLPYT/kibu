package work.lclpnet.kibu.access.mixin;

import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(TeamS2CPacket.class)
public interface TeamS2CPacketAccessor {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Accessor
    @Mutable
    void setTeam(Optional<TeamS2CPacket.SerializableTeam> team);
}
