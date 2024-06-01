package work.lclpnet.kibu.access.network.packet;

import net.minecraft.network.packet.s2c.play.WorldBorderWarningBlocksChangedS2CPacket;
import work.lclpnet.kibu.access.mixin.WorldBorderWarningBlocksChangedS2CPacketAccessor;

public class WorldBorderWarningBlocksChangedS2CPacketAccess {

    private WorldBorderWarningBlocksChangedS2CPacketAccess() {}

    public static WorldBorderWarningBlocksChangedS2CPacket withWarningBlocks(WorldBorderWarningBlocksChangedS2CPacket packet, int warningBlocks) {
        ((WorldBorderWarningBlocksChangedS2CPacketAccessor) packet).setWarningBlocks(warningBlocks);

        return packet;
    }
}
