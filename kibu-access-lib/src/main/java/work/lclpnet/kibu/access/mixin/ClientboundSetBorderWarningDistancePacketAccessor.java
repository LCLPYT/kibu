package work.lclpnet.kibu.access.mixin;

import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDistancePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientboundSetBorderWarningDistancePacket.class)
public interface ClientboundSetBorderWarningDistancePacketAccessor {

    @Mutable
    @Accessor
    void setWarningBlocks(int warningBlocks);
}
