package work.lclpnet.kibu.inv.type;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class RestrictedInventory extends KibuInventory {

    public RestrictedInventory(int rows, Text title) {
        super(rows, title);
    }

    public boolean canEdit(ServerPlayerEntity player) {
        return false;
    }
}
