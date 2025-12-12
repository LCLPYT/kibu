package work.lclpnet.kibu.access.network.packet;

import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDistancePacket;
import work.lclpnet.kibu.access.mixin.WorldBorderWarningBlocksChangedS2CPacketAccessor;

public class WorldBorderWarningBlocksChangedS2CPacketAccess {

    private WorldBorderWarningBlocksChangedS2CPacketAccess() {}

    public static ClientboundSetBorderWarningDistancePacket withWarningBlocks(ClientboundSetBorderWarningDistancePacket packet, int warningBlocks) {
        ((WorldBorderWarningBlocksChangedS2CPacketAccessor) packet).setWarningBlocks(warningBlocks);

        return packet;
    }
}
