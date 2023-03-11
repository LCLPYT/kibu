package work.lclpnet.kibu.hook.util;

import net.minecraft.entity.player.PlayerEntity;

public class PlayerUtils {

    public static void syncPlayerItems(PlayerEntity player) {
        player.playerScreenHandler.syncState();
    }
}
