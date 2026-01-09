package work.lclpnet.test;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fish.TropicalFish;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import work.lclpnet.kibu.access.VelocityModifier;
import work.lclpnet.kibu.access.entity.EntityAccess;
import work.lclpnet.kibu.access.entity.GoatEntityAccess;
import work.lclpnet.kibu.access.entity.TropicalFishEntityAccess;
import work.lclpnet.kibu.behaviour.entity.VexEntityBehaviour;
import work.lclpnet.kibu.hook.ServerMessageHooks;
import work.lclpnet.kibu.hook.entity.*;
import work.lclpnet.kibu.hook.entity.leash.*;
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

import static net.minecraft.world.item.Items.STICK;
import static work.lclpnet.kibu.access.entity.ServerPlayerAccess.playSoundToPlayer;

public class KibuTestMod implements ModInitializer {

    @Override
    public void onInitialize() {
        doubleJump();
        preventWithWitherRose();
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
            if (world.isClientSide() || hand != InteractionHand.MAIN_HAND || !(_player instanceof ServerPlayer player))
                return InteractionResult.PASS;

            return switch (player.getMainHandItem().getItem()) {
                case Item it when it == Items.BOOK -> {
                    TextPrompt.open(player, Component.literal("Input Text"), "Your text...", s -> !s.isBlank())
                            .thenAccept(val -> player.sendSystemMessage(Component.literal("You typed: " + val.orElse("nothing"))));

                    yield InteractionResult.SUCCESS_SERVER;
                }
                case Item it when it == Items.RESIN_CLUMP -> {
                    enum Opts { FOO, BAR }

                    OptionPrompt.open(player, Component.literal("Choose something"), List.of(Opts.FOO, Opts.BAR), opt -> switch (opt) {
                        case FOO -> new ItemStack(Items.DIAMOND);
                        case BAR -> new ItemStack(Items.EMERALD);
                    }).thenAccept(val -> player.sendSystemMessage(Component.literal("You chose: " + val.map(Opts::name).orElse("nothing"))));

                    yield InteractionResult.SUCCESS_SERVER;
                }
                case Item it when it == Items.NAUTILUS_SHELL -> {
                    var inv = new RestrictedInventory(2, Component.literal("Unmodifiable inventory"));

                    inv.setItem(3, new ItemStack(Items.DIAMOND_BLOCK, 11));
                    inv.setItem(14, new ItemStack(Items.ELYTRA));

                    player.openMenu(inv);

                    yield InteractionResult.SUCCESS_SERVER;
                }
                default -> InteractionResult.PASS;
            };
        });
    }

    private void teleportWithBrick() {
        PlayerInteractionHooks.USE_ITEM.register((player, world, hand) -> {
            if (!world.isClientSide() && player.getMainHandItem().is(Items.BRICK) && player instanceof ServerPlayer sp) {
                sp.teleportTo(sp.level(), sp.getX(), sp.getY() + 20, sp.getZ(), Set.of(), sp.getYRot(), sp.getXRot(), true);
                return InteractionResult.SUCCESS_SERVER;
            }

            return InteractionResult.PASS;
        });

        PlayerTeleportedCallback.HOOK.register(player -> System.out.printf("%s just teleported%n", player.getScoreboardName()));
    }

    private void preventBeyond300() {
        AffectedByDaylightCallback.HOOK.register(entity -> entity.getY() > 300);
        EntityTeleportCallback.HOOK.register((entity, x, y, z) -> entity.getY() > 300);
        ProjectileCanHitCallback.HOOK.register((projectile, entity) -> entity.getY() <= 300);
        FarmlandMoistureChangeCallback.HOOK.register((world, pos, moisture)
                -> pos.getY() > 300 && moisture < world.getBlockState(pos).getValue(FarmBlock.MOISTURE));
    }

    private void entityEditor() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ItemStack stack = player.getItemInHand(hand);

            if (stack.is(Items.BLAZE_ROD) && player instanceof ServerPlayer serverPlayer && !world.isClientSide() && hitResult != null) {
                toggleInvisibility(entity, serverPlayer);

                return InteractionResult.SUCCESS;
            }

            if (!stack.is(Items.NETHER_STAR)) {
                return InteractionResult.PASS;
            }

            if (entity instanceof Goat goat) {
                if (goat.hasLeftHorn() && goat.hasRightHorn()) {
                    GoatEntityAccess.setLeftHorn(goat, false);
                } else if (!goat.hasLeftHorn() && goat.hasRightHorn()) {
                    GoatEntityAccess.setRightHorn(goat, false);
                } else {
                    GoatEntityAccess.setLeftHorn(goat, true);
                    GoatEntityAccess.setRightHorn(goat, true);
                }
            }
            else if (entity instanceof TropicalFish tropicalFish) {
                TropicalFishEntityAccess.setVariant(tropicalFish, TropicalFish.Pattern.BETTY, DyeColor.BLUE, DyeColor.GREEN);
            }
            else if (entity instanceof Vex vex) {
                VexEntityBehaviour.setForceClipping(vex, !VexEntityBehaviour.isForceClipping(vex));
            }

            return InteractionResult.SUCCESS;
        });
    }

    private static void toggleInvisibility(Entity entity, ServerPlayer serverPlayer) {
        var tags = entity.getTags();
        boolean invisible = tags.contains("invisible");

        if (invisible) {
            tags.remove("invisible");
        } else {
            tags.add("invisible");
        }

        byte flags = entity.getEntityData().get(EntityAccess.FLAGS);
        flags = EntityAccess.setFlag(flags, EntityAccess.INVISIBLE_FLAG_INDEX, !invisible);

        var entry = SynchedEntityData.DataValue.create(EntityAccess.FLAGS, flags);
        var packet = new ClientboundSetEntityDataPacket(entity.getId(), List.of(entry));
        serverPlayer.connection.send(packet);
    }

    private void preventWhenRaining() {
        WorldPhysicsHooks.BLOCK_ITEM_DROP.register((world, pos, stack) -> world.isRaining());

        ItemScatterCallback.HOOK.register((world, x, y, z, stack) -> world.isRaining());

        EntityDropItemCallback.HOOK.register((world, entity, itemEntity) -> world.isRaining());

        EntityConvertCallback.HOOK.register((entity, type) -> entity.level().isRaining());

        WitherShootCallback.HOOK.register((wither, targetX, targetY, targetZ) -> wither.level().isRaining());

        WorldPhysicsHooks.CORAL_DEATH.register((world, pos) -> world.isRaining());

        WorldPhysicsHooks.EXPLOSION.register(explosion -> explosion.level().isRaining());
    }

    private void useSeparateMapsForNether() {
        MapStateCallback.HOOK.register((world, id) -> {
            if (!Level.NETHER.equals(world.dimension())) {
                return null;
            }

            return world.getMapData(id);
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

    private void preventWithWitherRose() {
        // cancel when holding a wither rose
        EntityHealthCallback.HOOK.register((entity, health) -> {
            // check if spawned in world yet
            if (entity.level().getEntity(entity.getId()) == null) return false;

            return cancelMainhandWitherRose(entity);
        });

        PlayerInventoryHooks.SWAP_HANDS.register((player, slot) -> cancelMainhandWitherRose(player));
    }

    private boolean cancelMainhandWitherRose(LivingEntity entity) {
        return entity instanceof ServerPlayer player && player.getItemInHand(InteractionHand.MAIN_HAND).is(Items.WITHER_ROSE);
    }

    private static void doubleJump() {
        // add double jump when holding a feather
        PlayerToggleFlightCallback.HOOK.register((player, fly) -> {
            if (player.getItemInHand(InteractionHand.MAIN_HAND).is(Items.FEATHER) && fly) {
                VelocityModifier.setVelocity(player, player.getLookAngle().scale(1.3));
                return true;
            }

            return false;
        });
    }

    private void preventWithStick() {
        NonLivingDamageCallback.HOOK.register((entity, source, amount) -> {
            if (!(source.getDirectEntity() instanceof ServerPlayer player)) return false;

            ItemStack stack = player.getMainHandItem();
            return stack.is(STICK);
        });

        ItemFramePutItemCallback.HOOK.register((itemFrame, stack, player, hand) -> stack.is(STICK));

        ItemFrameRotateCallback.HOOK.register((itemFrame, player, hand) -> {
            ItemStack stack = player.getItemInHand(hand);
            return stack.is(STICK);
        });

        ItemFrameRemoveItemCallback.HOOK.register((itemFrame, attacker) -> {
            if (!(attacker instanceof ServerPlayer player)) return false;

            ItemStack stack = player.getMainHandItem();
            return stack.is(STICK);
        });

        ArmorStandManipulateCallback.HOOK.register((armorStand, player, slot, stack, hand) -> player.getItemInHand(hand).is(STICK));

        ItemUseOnEntityCallback.HOOK.register((player, entity, hand, stack) -> cancelOffhandStick(player));

        LeashEntityCallback.HOOK.register((player, entity) -> cancelOffhandStick(player));
        UnleashEntityCallback.HOOK.register((player, entity) -> cancelOffhandStick(player));
        LeashDestroyCallback.HOOK.register((player, leashed) -> cancelOffhandStick(player));
        LeashEntitiesToBlockCallback.HOOK.register((player, pos, entities) -> cancelOffhandStick(player));
        LeashEntitiesToEntityCallback.HOOK.register((player, pos, entities) -> cancelOffhandStick(player));
        LeashKnotTakeCallback.HOOK.register((player, leashKnot) -> cancelOffhandStick(player));

        ProjectilePickupCallback.HOOK.register((player, projectile) -> player.getMainHandItem().is(STICK));

        BlockModificationHooks.EXTINGUISH_CANDLE.register((world, pos, entity)
                -> entity instanceof ServerPlayer player && player.getMainHandItem().is(STICK));

        PlayerAdvancementPacketCallback.HOOK.register((player, packet) -> player.getMainHandItem().is(STICK));

        PlayerRecipeNotificationCallback.HOOK.register((player, recipeEntry, displayEntry) -> player.getMainHandItem().is(STICK));

        BlockModificationHooks.DECORATIVE_POT_STORE.register((world, pos, entity)
                -> entity instanceof ServerPlayer player && cancelOffhandStick(player));

        ProjectileHooks.BREAK_DECORATED_POT.register((projectile, hit) -> {
            if (!(projectile.getOwner() instanceof ServerPlayer player)) return false;

            return cancelOffhandStick(player);
        });

        PlayerInventoryHooks.DROP_ITEM_ENTITY.register((player, itemEntity) -> cancelOffhandStick(player));

        PlayerInventoryHooks.DROPPED_ITEM_ENTITY.register((player, itemEntity) -> {
            if (player.getMainHandItem().is(STICK)) {
                System.out.println("DROPPED ITEM ENTITY " + itemEntity);
            }
        });

        CraftingRecipeCallback.HOOK.register((player, input, result) -> {
            // if the player is holding a stick and tries to craft sticks, the result will be empty, meaning no sticks can be crafted
            if (player.getMainHandItem().is(STICK) && result.is(STICK)) {
                return PendingResult.empty();
            }

            return PendingResult.pass();
        });

        CraftingRecipeCallback.HOOK.register((player, input, result) -> {
            // if the player is holding a stick and tries to craft a stone sword, a wooden sword will be the result
            if (player.getMainHandItem().is(STICK) && result.is(Items.STONE_SWORD)) {
                return PendingResult.of(new ItemStack(Items.WOODEN_SWORD));
            }

            return PendingResult.pass();
        });

        CraftingRecipeCallback.HOOK.register((player, input, result) -> {
            // if the player is holding a stick and tries to dye a bundle blue, the bundle will be dyed red instead using the transmute recipe
            if (player.getMainHandItem().is(STICK) && result.is(Items.BLUE_BUNDLE)) {
                MinecraftServer server = player.level().getServer();

                var key = ResourceKey.create(Registries.RECIPE, Identifier.parse("red_bundle"));

                return server.getRecipeManager().byKey(key)
                        .map(RecipeHolder::value)
                        .map(recipe -> recipe instanceof CraftingRecipe craftingRecipe ? craftingRecipe : null)
                        .map(craftingRecipe -> craftingRecipe.assemble(input, server.registryAccess()))
                        .map(PendingResult::of)
                        .orElse(PendingResult.pass());
            }

            return PendingResult.pass();
        });

        EntityDamageCallback.HOOK.register((entity, source, amount)
                -> entity instanceof ServerPlayer player
                && cancelOffhandStick(player));

        ServerMessageHooks.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> !sender.getMainHandItem().is(STICK));

        // disallow mobs to target players who hold a stick
        EntityTargetCallback.HOOK.register((entity, target)
                -> target instanceof ServerPlayer player
                && player.getMainHandItem().is(STICK));

        EntityStatusEffectCallback.HOOK.register((entity, effect, source)
                -> source != null
                && entity instanceof ServerPlayer player
                && player.getMainHandItem().is(STICK)
                && !effect.getEffect().value().isBeneficial());

        EntityBossBarCallback.HOOK.register((entity, bossBar, player) -> player.getMainHandItem().is(STICK));

        EntityMountCallback.HOOK.register((entity, vehicle, force) -> entity instanceof ServerPlayer player && player.getMainHandItem().is(STICK));
        EntityDismountCallback.HOOK.register((entity, vehicle) -> entity instanceof ServerPlayer player && player.getMainHandItem().is(STICK));

        ServerSendPacketCallback.HOOK.register((packet, handler)
                -> handler instanceof ServerGamePacketListenerImpl networkHandler
                && networkHandler.player.getItemInHand(InteractionHand.MAIN_HAND).is(STICK)
                && (packet instanceof ClientboundSoundPacket || packet instanceof ClientboundSoundEntityPacket)
                ? PendingResult.empty() : PendingResult.pass());

        WorldPhysicsHooks.REPLACE_DISK_ENCHANTMENT.register((world, pos, entity, state)
                -> entity instanceof ServerPlayer player && player.getMainHandItem().is(STICK));

        EntityUsePortalCallback.HOOK.register((entity, portal, pos)
                -> entity instanceof ServerPlayer player && player.getMainHandItem().is(STICK));

        PlayerWaypointCallback.HOOK.register((player, waypoint) -> player.getMainHandItem().is(STICK));
    }

    private boolean cancelOffhandStick(Player player) {
        return player.getOffhandItem().is(STICK);
    }

    private void misc() {
        PlayerSwingHandHook.HOOK.register((player, hand) -> System.out.println("player swings " + hand));

        // prevent all movement when holding an echo shard in the offhand
        PlayerMoveCallback.HOOK.register((player, from, to) -> {
            if (player.getMainHandItem().is(Items.POPPY) && player.level().getBlockState(player.blockPosition().below()).is(Blocks.DIAMOND_BLOCK)) {
                player.teleportTo(player.level(), player.getX(), player.getY() + 2, player.getZ(), Set.of(), 0f, 0f, true);
            }

            return player.getOffhandItem().is(Items.ECHO_SHARD);
        });

        PlayerInputCallback.HOOK.register((player, input) -> {
            if (player.getMainHandItem().is(Items.FEATHER)) {
                System.out.println("input " + input);
            }
        });

        PlayerJumpCallback.HOOK.register(player -> {
            if (player.getMainHandItem().is(Items.FEATHER)) {
                playSoundToPlayer(player, SoundEvents.NOTE_BLOCK_PLING.value(), SoundSource.MASTER, 0.2f, 1f);
                return cancelOffhandStick(player);
            }

            return false;
        });
    }
}
