package work.lclpnet.kibu.access.network.packet;

import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDistancePacket;
import work.lclpnet.kibu.access.mixin.ClientboundSetBorderWarningDistancePacketAccessor;

public class WorldBorderWarningBlocksChangedS2CPacketAccess {

    private WorldBorderWarningBlocksChangedS2CPacketAccess() {}

    public static ClientboundSetBorderWarningDistancePacket withWarningBlocks(ClientboundSetBorderWarningDistancePacket packet, int warningBlocks) {
        ((ClientboundSetBorderWarningDistancePacketAccessor) packet).setWarningBlocks(warningBlocks);

        return packet;
    }
}
