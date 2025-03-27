package work.lclpnet.test;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundFromEntityS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import work.lclpnet.kibu.access.VelocityModifier;
import work.lclpnet.kibu.access.entity.EntityAccess;
import work.lclpnet.kibu.access.entity.GoatEntityAccess;
import work.lclpnet.kibu.access.entity.TropicalFishEntityAccess;
import work.lclpnet.kibu.behaviour.entity.VexEntityBehaviour;
import work.lclpnet.kibu.hook.ServerMessageHooks;
import work.lclpnet.kibu.hook.entity.*;
import work.lclpnet.kibu.hook.network.ServerSendPacketCallback;
import work.lclpnet.kibu.hook.player.*;
import work.lclpnet.kibu.hook.util.PendingResult;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;
import work.lclpnet.kibu.hook.world.FarmlandMoistureChangeCallback;
import work.lclpnet.kibu.hook.world.ItemScatterCallback;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;
import work.lclpnet.kibu.inv.prompt.OptionPrompt;
import work.lclpnet.kibu.inv.prompt.TextPrompt;
import work.lclpnet.kibu.inv.type.RestrictedInventory;
import work.lclpnet.kibu.map.hook.MapStateCallback;

import java.util.List;
import java.util.Set;

import static net.minecraft.item.Items.STICK;

public class KibuTestMod implements ModInitializer {

    @Override
    public void onInitialize() {
        doubleJump();
        preventHealing();
        testCommands();
        preventWithStick();
        useSeparateMapsForNether();
        preventWhenRaining();
        entityEditor();
        preventBeyond300();
        teleportWithBrick();
        inventoryTests();
        misc();
    }

    private void inventoryTests() {
        PlayerInteractionHooks.USE_ITEM.register((_player, world, hand) -> {
            if (world.isClient || hand != Hand.MAIN_HAND || !(_player instanceof ServerPlayerEntity player))
                return ActionResult.PASS;

            return switch (player.getMainHandStack().getItem()) {
                case Item it when it == Items.BOOK -> {
                    TextPrompt.open(player, Text.literal("Input Text"), "Your text...", s -> !s.isBlank())
                            .thenAccept(val -> player.sendMessage(Text.literal("You typed: " + val.orElse("nothing"))));

                    yield ActionResult.SUCCESS_SERVER;
                }
                case Item it when it == Items.RESIN_CLUMP -> {
                    enum Opts { FOO, BAR }

                    OptionPrompt.open(player, Text.literal("Choose something"), List.of(Opts.FOO, Opts.BAR), opt -> switch (opt) {
                        case FOO -> new ItemStack(Items.DIAMOND);
                        case BAR -> new ItemStack(Items.EMERALD);
                    }).thenAccept(val -> player.sendMessage(Text.literal("You chose: " + val.map(Opts::name).orElse("nothing"))));

                    yield ActionResult.SUCCESS_SERVER;
                }
                case Item it when it == Items.NAUTILUS_SHELL -> {
                    var inv = new RestrictedInventory(2, Text.literal("Unmodifiable inventory"));

                    inv.setStack(3, new ItemStack(Items.DIAMOND_BLOCK, 11));
                    inv.setStack(14, new ItemStack(Items.ELYTRA));

                    player.openHandledScreen(inv);

                    yield ActionResult.SUCCESS_SERVER;
                }
                default -> ActionResult.PASS;
            };
        });
    }

    private void teleportWithBrick() {
        PlayerInteractionHooks.USE_ITEM.register((player, world, hand) -> {
            if (!world.isClient && player.getMainHandStack().isOf(Items.BRICK) && player instanceof ServerPlayerEntity sp) {
                sp.teleport(sp.getServerWorld(), sp.getX(), sp.getY() + 20, sp.getZ(), Set.of(), sp.getYaw(), sp.getPitch(), true);
                return ActionResult.SUCCESS_SERVER;
            }

            return ActionResult.PASS;
        });

        PlayerTeleportedCallback.HOOK.register(player -> System.out.printf("%s just teleported%n", player.getNameForScoreboard()));
    }

    private void preventBeyond300() {
        AffectedByDaylightCallback.HOOK.register(entity -> entity.getY() > 300);
        EntityTeleportCallback.HOOK.register((entity, x, y, z) -> entity.getY() > 300);
        ProjectileCanHitCallback.HOOK.register((projectile, entity) -> entity.getY() <= 300);
        FarmlandMoistureChangeCallback.HOOK.register((world, pos, moisture)
                -> pos.getY() > 300 && moisture < world.getBlockState(pos).get(FarmlandBlock.MOISTURE));
    }

    private void entityEditor() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ItemStack stack = player.getStackInHand(hand);

            if (stack.isOf(Items.BLAZE_ROD) && player instanceof ServerPlayerEntity serverPlayer && !world.isClient && hitResult != null) {
                toggleInvisibility(entity, serverPlayer);

                return ActionResult.SUCCESS;
            }

            if (!stack.isOf(Items.NETHER_STAR)) {
                return ActionResult.PASS;
            }

            if (entity instanceof GoatEntity goat) {
                if (goat.hasLeftHorn() && goat.hasRightHorn()) {
                    GoatEntityAccess.setLeftHorn(goat, false);
                } else if (!goat.hasLeftHorn() && goat.hasRightHorn()) {
                    GoatEntityAccess.setRightHorn(goat, false);
                } else {
                    GoatEntityAccess.setLeftHorn(goat, true);
                    GoatEntityAccess.setRightHorn(goat, true);
                }
            }
            else if (entity instanceof TropicalFishEntity tropicalFish) {
                TropicalFishEntityAccess.setVariant(tropicalFish, TropicalFishEntity.Variety.BETTY, DyeColor.BLUE, DyeColor.GREEN);
            }
            else if (entity instanceof VexEntity vex) {
                VexEntityBehaviour.setForceClipping(vex, !VexEntityBehaviour.isForceClipping(vex));
            }

            return ActionResult.SUCCESS;
        });
    }

    private static void toggleInvisibility(Entity entity, ServerPlayerEntity serverPlayer) {
        var tags = entity.getCommandTags();
        boolean invisible = tags.contains("invisible");

        if (invisible) {
            tags.remove("invisible");
        } else {
            tags.add("invisible");
        }

        byte flags = entity.getDataTracker().get(EntityAccess.FLAGS);
        flags = EntityAccess.setFlag(flags, EntityAccess.INVISIBLE_FLAG_INDEX, !invisible);

        var entry = DataTracker.SerializedEntry.of(EntityAccess.FLAGS, flags);
        var packet = new EntityTrackerUpdateS2CPacket(entity.getId(), List.of(entry));
        serverPlayer.networkHandler.sendPacket(packet);
    }

    private void preventWhenRaining() {
        WorldPhysicsHooks.BLOCK_ITEM_DROP.register((world, pos, stack) -> world.isRaining());

        ItemScatterCallback.HOOK.register((world, x, y, z, stack) -> world.isRaining());

        EntityDropItemCallback.HOOK.register((world, entity, itemEntity) -> world.isRaining());

        EntityConvertCallback.HOOK.register((entity, type) -> entity.getWorld().isRaining());

        WitherShootCallback.HOOK.register((wither, targetX, targetY, targetZ) -> wither.getWorld().isRaining());

        WorldPhysicsHooks.CORAL_DEATH.register((world, pos) -> world.isRaining());

        WorldPhysicsHooks.EXPLOSION.register(explosion -> explosion.getWorld().isRaining());
    }

    private void useSeparateMapsForNether() {
        MapStateCallback.HOOK.register((world, id) -> {
            if (!World.NETHER.equals(world.getRegistryKey())) {
                return null;
            }

            return world.getPersistentStateManager().get(MapState.getPersistentStateType(), id.asString());
        });
    }

    private void testCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            new ImageMapCommand().register(dispatcher);
            new SchematicCommand().register(dispatcher);
            new BehaviourCommand().register(dispatcher);
            new TeamCommand().register(dispatcher);
            new BorderCommand().register(dispatcher);
        });
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
            return stack.isOf(STICK);
        });

        ItemFramePutItemCallback.HOOK.register((itemFrame, stack, player, hand) -> stack.isOf(STICK));

        ItemFrameRotateCallback.HOOK.register((itemFrame, player, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            return stack.isOf(STICK);
        });

        ItemFrameRemoveItemCallback.HOOK.register((itemFrame, attacker) -> {
            if (!(attacker instanceof ServerPlayerEntity player)) return false;

            ItemStack stack = player.getMainHandStack();
            return stack.isOf(STICK);
        });

        ArmorStandManipulateCallback.HOOK.register((armorStand, player, slot, stack, hand) -> player.getStackInHand(hand).isOf(STICK));

        ItemUseOnEntityCallback.HOOK.register((player, entity, hand, stack) -> player.getOffHandStack().isOf(STICK));

        LeashAttachCallback.HOOK.register((player, world, pos) -> player.getOffHandStack().isOf(STICK));

        LeashDetachCallback.HOOK.register((player, leashKnot) -> player.getOffHandStack().isOf(STICK));

        LeashEntityCallback.HOOK.register((player, entity) -> player.getOffHandStack().isOf(STICK));

        UnleashEntityCallback.HOOK.register((player, entity) -> player.getOffHandStack().isOf(STICK));

        LeashEntityToBlockCallback.HOOK.register((player, entity, leashKnot) -> player.getOffHandStack().isOf(STICK));

        ProjectilePickupCallback.HOOK.register((player, projectile) -> player.getMainHandStack().isOf(STICK));

        BlockModificationHooks.EXTINGUISH_CANDLE.register((world, pos, entity)
                -> entity instanceof ServerPlayerEntity player && player.getMainHandStack().isOf(STICK));

        PlayerAdvancementPacketCallback.HOOK.register((player, packet) -> player.getMainHandStack().isOf(STICK));

        PlayerRecipeNotificationCallback.HOOK.register((player, recipeEntry, displayEntry) -> player.getMainHandStack().isOf(STICK));

        BlockModificationHooks.DECORATIVE_POT_STORE.register((world, pos, entity)
                -> entity instanceof ServerPlayerEntity player && player.getOffHandStack().isOf(STICK));

        ProjectileHooks.BREAK_DECORATED_POT.register((projectile, hit) -> {
            if (!(projectile.getOwner() instanceof ServerPlayerEntity player)) return false;

            return player.getOffHandStack().isOf(STICK);
        });

        PlayerInventoryHooks.DROP_ITEM_ENTITY.register((player, itemEntity) -> player.getOffHandStack().isOf(STICK));

        PlayerInventoryHooks.DROPPED_ITEM_ENTITY.register((player, itemEntity) -> {
            if (player.getMainHandStack().isOf(STICK)) {
                System.out.println("DROPPED ITEM ENTITY " + itemEntity);
            }
        });

        CraftingRecipeCallback.HOOK.register((player, input, result) -> {
            // if the player is holding a stick and tries to craft sticks, the result will be empty, meaning no sticks can be crafted
            if (player.getMainHandStack().isOf(STICK) && result.isOf(STICK)) {
                return PendingResult.empty();
            }

            return PendingResult.pass();
        });

        CraftingRecipeCallback.HOOK.register((player, input, result) -> {
            // if the player is holding a stick and tries to craft a stone sword, a wooden sword will be the result
            if (player.getMainHandStack().isOf(STICK) && result.isOf(Items.STONE_SWORD)) {
                return PendingResult.of(new ItemStack(Items.WOODEN_SWORD));
            }

            return PendingResult.pass();
        });

        CraftingRecipeCallback.HOOK.register((player, input, result) -> {
            // if the player is holding a stick and tries to dye a bundle blue, the bundle will be dyed red instead using the transmute recipe
            if (player.getMainHandStack().isOf(STICK) && result.isOf(Items.BLUE_BUNDLE)) {
                MinecraftServer server = player.getServer();

                if (server != null) {
                    var key = RegistryKey.of(RegistryKeys.RECIPE, Identifier.of("red_bundle"));

                    return server.getRecipeManager().get(key)
                            .map(RecipeEntry::value)
                            .map(recipe -> recipe instanceof CraftingRecipe craftingRecipe ? craftingRecipe : null)
                            .map(craftingRecipe -> craftingRecipe.craft(input, server.getRegistryManager()))
                            .map(PendingResult::of)
                            .orElse(PendingResult.pass());
                }
            }

            return PendingResult.pass();
        });

        EntityDamageCallback.HOOK.register((entity, source, amount)
                -> entity instanceof ServerPlayerEntity player
                && player.getOffHandStack().isOf(STICK));

        ServerMessageHooks.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> !sender.getMainHandStack().isOf(STICK));

        // disallow mobs to target players who hold a stick
        EntityTargetCallback.HOOK.register((entity, target)
                -> target instanceof ServerPlayerEntity player
                && player.getMainHandStack().isOf(STICK));

        EntityStatusEffectCallback.HOOK.register((entity, effect, source)
                -> source != null
                && entity instanceof ServerPlayerEntity player
                && player.getMainHandStack().isOf(STICK)
                && !effect.getEffectType().value().isBeneficial());

        EntityBossBarCallback.HOOK.register((entity, bossBar, player) -> player.getMainHandStack().isOf(STICK));

        EntityMountCallback.HOOK.register((entity, vehicle, force) -> entity instanceof ServerPlayerEntity player && player.getMainHandStack().isOf(STICK));
        EntityDismountCallback.HOOK.register((entity, vehicle) -> entity instanceof ServerPlayerEntity player && player.getMainHandStack().isOf(STICK));

        ServerSendPacketCallback.HOOK.register((packet, handler)
                -> handler instanceof ServerPlayNetworkHandler networkHandler
                && networkHandler.player.getStackInHand(Hand.MAIN_HAND).isOf(STICK)
                && (packet instanceof PlaySoundS2CPacket || packet instanceof PlaySoundFromEntityS2CPacket)
                ? PendingResult.empty() : PendingResult.pass());

        WorldPhysicsHooks.REPLACE_DISK_ENCHANTMENT.register((world, pos, entity, state)
                -> entity instanceof ServerPlayerEntity player && player.getMainHandStack().isOf(STICK));

        EntityUsePortalCallback.HOOK.register((entity, portal, pos)
                -> entity instanceof ServerPlayerEntity player && player.getMainHandStack().isOf(STICK));
    }

    private void misc() {
        PlayerSwingHandHook.HOOK.register((player, hand) -> System.out.println("player swings " + hand));

        // prevent all movement when holding an echo shard in the offhand
        PlayerMoveCallback.HOOK.register((player, from, to) -> {
            if (player.getMainHandStack().isOf(Items.POPPY) && player.getWorld().getBlockState(player.getBlockPos().down()).isOf(Blocks.DIAMOND_BLOCK)) {
                player.teleport(player.getServerWorld(), player.getX(), player.getY() + 2, player.getZ(), Set.of(), 0f, 0f, true);
            }

            return player.getOffHandStack().isOf(Items.ECHO_SHARD);
        });

        PlayerInputCallback.HOOK.register((player, input) -> {
            if (player.getMainHandStack().isOf(Items.FEATHER)) {
                System.out.println("input " + input);
            }
        });

        PlayerJumpCallback.HOOK.register(player -> {
            if (player.getMainHandStack().isOf(Items.FEATHER)) {
                player.playSoundToPlayer(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), SoundCategory.MASTER, 0.2f, 1f);
                return player.getOffHandStack().isOf(STICK);
            }

            return false;
        });
    }
}
