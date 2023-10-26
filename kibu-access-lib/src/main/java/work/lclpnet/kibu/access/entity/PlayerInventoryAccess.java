package work.lclpnet.kibu.access.entity;

import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerInventoryAccess {

    public static void setSelectedSlot(ServerPlayerEntity player, int slot) {
        player.getInventory().selectedSlot = Math.min(8, Math.max(0, slot));
        updateSelectedSlot(player);
    }

    public static void updateSelectedSlot(ServerPlayerEntity player) {
        var packet = new UpdateSelectedSlotS2CPacket(player.getInventory().selectedSlot);
        player.networkHandler.sendPacket(packet);
    }

    private PlayerInventoryAccess() {}
}
