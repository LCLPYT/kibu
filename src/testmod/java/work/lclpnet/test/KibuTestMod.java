package work.lclpnet.test;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import work.lclpnet.kibu.access.VelocityModifier;
import work.lclpnet.kibu.hook.player.PlayerToggleFlightCallback;

public class KibuTestMod implements ModInitializer {

    @Override
    public void onInitialize() {
        PlayerToggleFlightCallback.HOOK.register((player, fly) -> {
            if (player.getStackInHand(Hand.MAIN_HAND).isOf(Items.FEATHER) && fly) {
                VelocityModifier.setVelocity(player, player.getRotationVector().multiply(1.3));
                return true;
            }

            return false;
        });
    }
}
