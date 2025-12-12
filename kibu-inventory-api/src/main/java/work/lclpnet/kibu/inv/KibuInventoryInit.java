package work.lclpnet.kibu.inv;

import net.fabricmc.api.ModInitializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
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
        ServerPlayer player = event.player();

        if (event.player().containerMenu instanceof TextPrompt.Handler handler) {
            handler.onClick(event);
            return true;
        }

        if (event.inventory() instanceof OptionPrompt.Handler handler) {
            handler.onClick(event);
            return true;
        }

        Container source = event.inventory();
        Container target = event.targetInventory();

        if (source instanceof RestrictedInventory inv) {
            return !inv.canEdit(player);
        }

        if (target instanceof RestrictedInventory inv) {
            return !inv.canEdit(player);
        }

        return false;
    }
}
