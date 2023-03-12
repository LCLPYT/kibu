package work.lclpnet.kibu.hook.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerUtils {

    public static void syncPlayerItems(PlayerEntity player) {
        player.playerScreenHandler.syncState();
    }

    public static void syncPlayerHealthAndHunger(ServerPlayerEntity player) {
        var hungerManager = player.getHungerManager();
        var packet = new HealthUpdateS2CPacket(player.getHealth(), hungerManager.getFoodLevel(), hungerManager.getSaturationLevel());
        player.networkHandler.sendPacket(packet);
    }
}
