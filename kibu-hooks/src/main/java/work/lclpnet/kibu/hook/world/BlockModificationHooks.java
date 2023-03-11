package work.lclpnet.kibu.hook.world;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockModificationHooks {

    public static final Event<PlaceBlockHook> PLACE_BLOCK = EventFactory.createArrayBacked(PlaceBlockHook.class, callbacks -> (world, pos, entity, newState) -> {
        for (var callback : callbacks)
            if (callback.onPlace(world, pos, entity, newState))
                return true;

        return false;
    });

    public static final Event<BlockModifyHook> BREAK_BLOCK = EventFactory.createArrayBacked(BlockModifyHook.class, callbacks -> (world, pos, entity) -> {
        for (var callback : callbacks)
            if (callback.onModify(world, pos, entity))
                return true;

        return false;
    });

    public static final Event<FluidTransferHook> PLACE_FLUID = EventFactory.createArrayBacked(FluidTransferHook.class, callbacks -> (world, pos, entity, fluid) -> {
        for (var callback : callbacks)
            if (callback.onTransfer(world, pos, entity, fluid))
                return true;

        return false;
    });

    public static final Event<FluidTransferHook> PICKUP_FLUID = EventFactory.createArrayBacked(FluidTransferHook.class, callbacks -> (world, pos, entity, fluid) -> {
        for (var callback : callbacks)
            if (callback.onTransfer(world, pos, entity, fluid))
                return true;

        return false;
    });

    public static final Event<BlockModifyHook> FARMLAND_TRAMPLE = EventFactory.createArrayBacked(BlockModifyHook.class, callbacks -> (world, pos, entity) -> {
        for (var callback : callbacks)
            if (callback.onModify(world, pos, entity))
                return true;

        return false;
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

    static {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            // propagate, when not cancelled
            return !BlockModificationHooks.BREAK_BLOCK.invoker().onModify(world, pos, player);
        });
    }
}
