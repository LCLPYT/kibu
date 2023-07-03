package work.lclpnet.kibu.inv;

import net.fabricmc.api.ModInitializer;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.hook.player.PlayerInventoryHooks;
import work.lclpnet.kibu.inv.type.RestrictedInventory;

public class KibuInventoryInit implements ModInitializer {

    @Override
    public void onInitialize() {
        PlayerInventoryHooks.MODIFY_INVENTORY.register(event -> {
            ServerPlayerEntity player = event.player();
            Inventory source = event.inventory();
            Inventory target = event.targetInventory();

            if (source instanceof RestrictedInventory inv) {
                return !inv.canEdit(player);
            }

            if (target instanceof RestrictedInventory inv) {
                return !inv.canEdit(player);
            }

            return false;
        });
    }
}
