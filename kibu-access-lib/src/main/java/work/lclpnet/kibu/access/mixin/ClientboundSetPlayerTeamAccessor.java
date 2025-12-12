package work.lclpnet.kibu.access.mixin;

import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(ClientboundSetPlayerTeamPacket.class)
public interface ClientboundSetPlayerTeamAccessor {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Accessor
    @Mutable
    void setParameters(Optional<ClientboundSetPlayerTeamPacket.Parameters> team);
}
