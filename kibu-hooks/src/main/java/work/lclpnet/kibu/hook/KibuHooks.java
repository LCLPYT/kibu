package work.lclpnet.kibu.hook;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.CandleBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.hook.player.PlayerDeathCallback;
import work.lclpnet.kibu.hook.util.PlayerUtils;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;

public class KibuHooks implements ModInitializer {

    @Override
    public void onInitialize() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            // propagate, when not cancelled
            return !BlockModificationHooks.BREAK_BLOCK.invoker().onModify(world, pos, player);
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            final var state = world.getBlockState(hitResult.getBlockPos());

            if (state.isOf(Blocks.CAKE)) {
                return onUseCake(player, hand, hitResult);
            }

            if (state.isIn(BlockTags.FLOWER_POTS)) {
                return onUseFlowerPot(player, hand, hitResult);
            }

            if (state.isOf(Blocks.PUMPKIN)) {
                return onUsePumpkin(player, hand, hitResult);
            }

            return ActionResult.PASS;
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (entity instanceof ServerPlayerEntity player) {
                PlayerDeathCallback.HOOK.invoker().onDeath(player, damageSource);
            }
        });
    }

    @NotNull
    private static ActionResult onUseCake(PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        final var itemStack = player.getStackInHand(hand);
        final var pos = hitResult.getBlockPos();
        final var world = player.getWorld();
        final var state = world.getBlockState(pos);

        if (itemStack.isIn(ItemTags.CANDLES) && state.get(CakeBlock.BITES) == 0 && Block.getBlockFromItem(itemStack.getItem()) instanceof CandleBlock) {
            ActionResult result = invokeItemUseBlock(player, hand, hitResult);
            if (result != null) return result;

            return ActionResult.PASS;
        }

        // player tries to eat the cake
        if (!BlockModificationHooks.EAT_CAKE.invoker().onModify(world, pos, player)) return ActionResult.PASS;

        if (player instanceof ServerPlayerEntity serverPlayer) {
            PlayerUtils.syncPlayerHealthAndHunger(serverPlayer);
        }

        return ActionResult.FAIL;
    }

    @NotNull
    private static ActionResult onUseFlowerPot(PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        ActionResult result = invokeItemUseBlock(player, hand, hitResult);
        if (result != null) return result;

        return ActionResult.PASS;
    }

    @NotNull
    private static ActionResult onUsePumpkin(PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        var stack = player.getStackInHand(hand);
        if (!stack.isOf(Items.SHEARS)) return ActionResult.PASS;

        ActionResult result = invokeItemUseBlock(player, hand, hitResult);
        if (result != null) return result;

        return ActionResult.PASS;
    }

    @Nullable
    private static ActionResult invokeItemUseBlock(PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        var ctx = new ItemUsageContext(player, hand, hitResult);
        var result = BlockModificationHooks.USE_ITEM_ON_BLOCK.invoker().onUse(ctx);

        if (result != null) {
            // when useOnBlock is cancelled, sync the item consumption cancel with the client
            if (!player.isCreative() && !player.isSpectator()) {
                PlayerUtils.syncPlayerItems(player);
            }

            return result == ActionResult.PASS ? ActionResult.FAIL : result;
        }

        return null;
    }
}
