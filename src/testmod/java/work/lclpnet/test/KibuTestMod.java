package work.lclpnet.test;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import work.lclpnet.kibu.access.VelocityModifier;
import work.lclpnet.kibu.hook.entity.*;
import work.lclpnet.kibu.hook.player.PlayerToggleFlightCallback;

public class KibuTestMod implements ModInitializer {

    @Override
    public void onInitialize() {
        doubleJump();
        preventHealing();
        testCommands();
        preventWithStick();
    }

    private void testCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment)
                -> new ImageMapCommand().register(dispatcher));
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

    private void preventWithStick() {
        DecorationEntityDamageCallback.HOOK.register((entity, source, amount) -> {
            if (!(source.getSource() instanceof ServerPlayerEntity player)) return false;

            ItemStack stack = player.getMainHandStack();
            return stack.isOf(Items.STICK);
        });

        ItemFramePutItemCallback.HOOK.register((itemFrame, stack, player, hand) -> stack.isOf(Items.STICK));

        ItemFrameRotateCallback.HOOK.register((itemFrame, player, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            return stack.isOf(Items.STICK);
        });

        ItemFrameDamageCallback.HOOK.register((itemFrame, source, amount) -> {
            if (!(source.getSource() instanceof ServerPlayerEntity player)) return false;

            ItemStack stack = player.getMainHandStack();
            return stack.isOf(Items.STICK);
        });

        ArmorStandManipulateCallback.HOOK.register((armorStand, player, slot, stack, hand) -> player.getStackInHand(hand).isOf(Items.STICK));

        MinecartDamageCallback.HOOK.register((minecart, source, amount) -> {
            if (!(source.getSource() instanceof ServerPlayerEntity player)) return false;

            ItemStack stack = player.getMainHandStack();
            return stack.isOf(Items.STICK);
        });

        BoatDamageCallback.HOOK.register((boat, source, amount) -> {
            if (!(source.getSource() instanceof ServerPlayerEntity player)) return false;

            ItemStack stack = player.getMainHandStack();
            return stack.isOf(Items.STICK);
        });
    }
}
