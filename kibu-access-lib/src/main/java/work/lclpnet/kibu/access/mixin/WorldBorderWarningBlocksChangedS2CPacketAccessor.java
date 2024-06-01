package work.lclpnet.kibu.access.mixin;

import net.minecraft.network.packet.s2c.play.WorldBorderWarningBlocksChangedS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldBorderWarningBlocksChangedS2CPacket.class)
public interface WorldBorderWarningBlocksChangedS2CPacketAccessor {

    @Mutable
    @Accessor
    void setWarningBlocks(int warningBlocks);
}
