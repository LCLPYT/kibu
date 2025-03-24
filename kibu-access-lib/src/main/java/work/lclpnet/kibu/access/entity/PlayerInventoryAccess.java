package work.lclpnet.kibu.access.entity;

import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class PlayerInventoryAccess {

    public static void setSelectedSlot(ServerPlayerEntity player, int slot) {
        slot = min(8, max(0, slot));
        player.getInventory().setSelectedSlot(slot);
        updateSelectedSlot(player);
    }

    public static void updateSelectedSlot(ServerPlayerEntity player) {
        var packet = new UpdateSelectedSlotS2CPacket(player.getInventory().getSelectedSlot());
        player.networkHandler.sendPacket(packet);
    }

    private PlayerInventoryAccess() {}
}
