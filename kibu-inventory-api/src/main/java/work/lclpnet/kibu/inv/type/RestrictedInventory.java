package work.lclpnet.kibu.inv.type;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class RestrictedInventory extends KibuInventory {

    public RestrictedInventory(int rows, Component title) {
        super(rows, title);
    }

    public boolean canEdit(ServerPlayer player) {
        return false;
    }
}
