package work.lclpnet.kibu.inv;

import net.fabricmc.api.ModInitializer;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.hook.player.PlayerInventoryHooks;
import work.lclpnet.kibu.inv.prompt.OptionPrompt;
import work.lclpnet.kibu.inv.prompt.TextPrompt;
import work.lclpnet.kibu.inv.type.RestrictedInventory;

public class KibuInventoryInit implements ModInitializer {

    @Override
    public void onInitialize() {
        PlayerInventoryHooks.MODIFY_INVENTORY.register(KibuInventoryInit::onModify);
    }

    private static boolean onModify(PlayerInventoryHooks.ClickEvent event) {
        ServerPlayerEntity player = event.player();

        if (event.player().currentScreenHandler instanceof TextPrompt.Handler handler) {
            handler.onClick(event);
            return true;
        }

        if (event.inventory() instanceof OptionPrompt.Handler handler) {
            handler.onClick(event);
            return true;
        }

        Inventory source = event.inventory();
        Inventory target = event.targetInventory();

        if (source instanceof RestrictedInventory inv) {
            return !inv.canEdit(player);
        }

        if (target instanceof RestrictedInventory inv) {
            return !inv.canEdit(player);
        }

        return false;
    }
}
