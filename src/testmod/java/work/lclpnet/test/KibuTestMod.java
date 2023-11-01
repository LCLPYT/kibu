package work.lclpnet.test;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import work.lclpnet.kibu.access.VelocityModifier;
import work.lclpnet.kibu.hook.entity.EntityHealthCallback;
import work.lclpnet.kibu.hook.player.PlayerToggleFlightCallback;

public class KibuTestMod implements ModInitializer {

    @Override
    public void onInitialize() {
        doubleJump();
        preventHealing();
    }

    private void preventHealing() {
        // cancel when holding a wither rose
        EntityHealthCallback.HOOK.register((entity, health)
                -> entity instanceof ServerPlayerEntity player && player.getInventory() != null
                   && player.getStackInHand(Hand.MAIN_HAND).isOf(Items.WITHER_ROSE));
    }

    private static void doubleJump() {
        // add double jump when holding a feather
        PlayerToggleFlightCallback.HOOK.register((player, fly) -> {
            if (player.getStackInHand(Hand.MAIN_HAND).isOf(Items.FEATHER) && fly) {
                VelocityModifier.setVelocity(player, player.getRotationVector().multiply(1.3));
                return true;
            }

            return false;
        });
    }
}
