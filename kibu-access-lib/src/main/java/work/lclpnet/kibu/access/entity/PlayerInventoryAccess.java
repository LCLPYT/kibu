package work.lclpnet.kibu.access.entity;

import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.access.mixin.PlayerInventoryAccessor;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class PlayerInventoryAccess {

    public static int getSelectedSlot(ServerPlayerEntity player) {
        return ((PlayerInventoryAccessor) player.getInventory()).getSelectedSlot();
    }

    public static void setSelectedSlot(ServerPlayerEntity player, int slot) {
        slot = min(8, max(0, slot));
        ((PlayerInventoryAccessor) player.getInventory()).setSelectedSlot(slot);
        updateSelectedSlot(player);
    }

    public static void updateSelectedSlot(ServerPlayerEntity player) {
        var packet = new UpdateSelectedSlotS2CPacket(getSelectedSlot(player));
        player.networkHandler.sendPacket(packet);
    }

    private PlayerInventoryAccess() {}
}
