package work.lclpnet.test;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import work.lclpnet.kibu.access.VelocityModifier;
import work.lclpnet.kibu.hook.entity.*;
import work.lclpnet.kibu.hook.player.PlayerAdvancementPacketCallback;
import work.lclpnet.kibu.hook.player.PlayerToggleFlightCallback;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;

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
        NonLivingDamageCallback.HOOK.register((entity, source, amount) -> {
            if (!(source.getSource() instanceof ServerPlayerEntity player)) return false;

            ItemStack stack = player.getMainHandStack();
            return stack.isOf(Items.STICK);
        });

        ItemFramePutItemCallback.HOOK.register((itemFrame, stack, player, hand) -> stack.isOf(Items.STICK));

        ItemFrameRotateCallback.HOOK.register((itemFrame, player, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            return stack.isOf(Items.STICK);
        });

        ItemFrameRemoveItemCallback.HOOK.register((itemFrame, attacker) -> {
            if (!(attacker instanceof ServerPlayerEntity player)) return false;

            ItemStack stack = player.getMainHandStack();
            return stack.isOf(Items.STICK);
        });

        ArmorStandManipulateCallback.HOOK.register((armorStand, player, slot, stack, hand) -> player.getStackInHand(hand).isOf(Items.STICK));

        ItemUseOnEntityCallback.HOOK.register((player, entity, hand, stack) -> player.getOffHandStack().isOf(Items.STICK));

        LeashAttachCallback.HOOK.register((player, world, pos) -> player.getOffHandStack().isOf(Items.STICK));

        LeashDetachCallback.HOOK.register((player, leashKnot) -> player.getOffHandStack().isOf(Items.STICK));

        LeashEntityCallback.HOOK.register((player, entity) -> player.getOffHandStack().isOf(Items.STICK));

        UnleashEntityCallback.HOOK.register((player, entity) -> player.getOffHandStack().isOf(Items.STICK));

        LeashEntityToBlockCallback.HOOK.register((player, entity, leashKnot) -> player.getOffHandStack().isOf(Items.STICK));

        ProjectilePickupCallback.HOOK.register((player, projectile) -> player.getMainHandStack().isOf(Items.STICK));

        BlockModificationHooks.EXTINGUISH_CANDLE.register((world, pos, entity)
                -> entity instanceof ServerPlayerEntity player && player.getMainHandStack().isOf(Items.STICK));

        PlayerAdvancementPacketCallback.HOOK.register((player, packet) -> player.getMainHandStack().isOf(Items.STICK));
    }
}
