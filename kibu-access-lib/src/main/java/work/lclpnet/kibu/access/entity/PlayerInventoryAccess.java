package work.lclpnet.kibu.access.entity;

import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
import net.minecraft.server.level.ServerPlayer;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class PlayerInventoryAccess {

    public static void setSelectedSlot(ServerPlayer player, int slot) {
        slot = min(8, max(0, slot));
        player.getInventory().setSelectedSlot(slot);
        updateSelectedSlot(player);
    }

    public static void updateSelectedSlot(ServerPlayer player) {
        var packet = new ClientboundSetHeldSlotPacket(player.getInventory().getSelectedSlot());
        player.connection.send(packet);
    }

    private PlayerInventoryAccess() {}
}
