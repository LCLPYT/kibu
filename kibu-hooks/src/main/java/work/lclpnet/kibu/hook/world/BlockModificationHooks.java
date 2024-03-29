package work.lclpnet.kibu.hook.world;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

import javax.annotation.Nullable;

public class BlockModificationHooks {

    private BlockModificationHooks() {}

    public static final Hook<PlaceBlockHook> PLACE_BLOCK = HookFactory.createArrayBacked(PlaceBlockHook.class, callbacks -> (world, pos, entity, newState) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onPlace(world, pos, entity, newState))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<BlockModifyHook> BREAK_BLOCK = HookFactory.createArrayBacked(BlockModifyHook.class, callbacks -> (world, pos, entity) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onModify(world, pos, entity))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<FluidTransferHook> PLACE_FLUID = HookFactory.createArrayBacked(FluidTransferHook.class, callbacks -> (world, pos, entity, fluid) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onTransfer(world, pos, entity, fluid))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<FluidTransferHook> PICKUP_FLUID = HookFactory.createArrayBacked(FluidTransferHook.class, callbacks -> (world, pos, entity, fluid) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onTransfer(world, pos, entity, fluid))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<BlockModifyHook> TRAMPLE_FARMLAND = HookFactory.createArrayBacked(BlockModifyHook.class, callbacks -> (world, pos, entity) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onModify(world, pos, entity))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<BlockModifyHook> TRAMPLE_TURTLE_EGG = HookFactory.createArrayBacked(BlockModifyHook.class, callbacks -> (world, pos, entity) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onModify(world, pos, entity))
                cancelled = true;

        return cancelled;
    });

    /**
     * Called when a player tries to use an item on a block.
     * E.g. placing a block or carving a log with an axe.
     * This is a special case of {@link net.fabricmc.fabric.api.event.player.UseBlockCallback}, but with an item.
     * This hook is fired before PLACE_BLOCK
     */
    public static final Hook<ItemUseOnBlock> USE_ITEM_ON_BLOCK = HookFactory.createArrayBacked(ItemUseOnBlock.class, callbacks -> (ctx) -> {
        for (var callback : callbacks) {
            var result = callback.onUse(ctx);

            if (result != null) return result;
        }

        return null;
    });

    public static final Hook<BlockModifyHook> EAT_CAKE = HookFactory.createArrayBacked(BlockModifyHook.class, callbacks -> (world, pos, entity) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onModify(world, pos, entity))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<BlockModifyHook> CAN_MOB_GRIEF = HookFactory.createArrayBacked(BlockModifyHook.class, callbacks -> (world, pos, entity) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onModify(world, pos, entity))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<BlockModifyHook> EXTINGUISH_CANDLE = HookFactory.createArrayBacked(BlockModifyHook.class, callbacks -> (world, pos, entity) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onModify(world, pos, entity))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<BlockModifyHook> COMPOSTER = HookFactory.createArrayBacked(BlockModifyHook.class, callbacks -> (world, pos, entity) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onModify(world, pos, entity))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<BlockModifyHook> CHARGE_RESPAWN_ANCHOR = HookFactory.createArrayBacked(BlockModifyHook.class, callbacks -> (world, pos, entity) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onModify(world, pos, entity))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<BlockModifyHook> EXPLODE_RESPAWN_LOCATION = HookFactory.createArrayBacked(BlockModifyHook.class, callbacks -> (world, pos, entity) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onModify(world, pos, entity))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<BlockModifyHook> PRIME_TNT = HookFactory.createArrayBacked(BlockModifyHook.class, callbacks -> (world, pos, entity) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onModify(world, pos, entity))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<BlockModifyHook> TAKE_LECTERN_BOOK = HookFactory.createArrayBacked(BlockModifyHook.class, callbacks -> (world, pos, entity) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onModify(world, pos, entity))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<BlockModifyHook> EDIT_SIGN = HookFactory.createArrayBacked(BlockModifyHook.class, callbacks -> (world, pos, entity) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onModify(world, pos, entity))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<BlockModifyHook> DECORATIVE_POT_STORE = HookFactory.createArrayBacked(BlockModifyHook.class, callbacks -> (world, pos, entity) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onModify(world, pos, entity))
                cancelled = true;

        return cancelled;
    });

    public interface PlaceBlockHook {
        boolean onPlace(World world, BlockPos pos, Entity entity, BlockState newState);
    }

    public interface BlockModifyHook {
        boolean onModify(World world, BlockPos pos, Entity entity);
    }

    public interface FluidTransferHook {
        boolean onTransfer(World world, BlockPos pos, Entity entity, Fluid fluid);
    }

    public interface ItemUseOnBlock {
        @Nullable
        ActionResult onUse(ItemUsageContext ctx);
    }
}
