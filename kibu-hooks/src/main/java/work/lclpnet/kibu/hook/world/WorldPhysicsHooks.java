package work.lclpnet.kibu.hook.world;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public class WorldPhysicsHooks {

    public static final Hook<ExplosionHook> EXPLOSION = HookFactory.createArrayBacked(ExplosionHook.class, callbacks -> exploder -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onExplode(exploder))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<FadeHook> MELT = HookFactory.createArrayBacked(FadeHook.class, callbacks -> (world, pos) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onFade(world, pos))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<FrostWalkerFreezeHook> FROST_WALKER_FREEZE = HookFactory.createArrayBacked(FrostWalkerFreezeHook.class, callbacks -> (world, pos, entity) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onFreeze(world, pos, entity))
                return cancelled;

        return cancelled;
    });

    public static final Hook<FadeHook> FREEZE = HookFactory.createArrayBacked(FadeHook.class, callbacks -> (world, pos) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onFade(world, pos))
                return cancelled;

        return cancelled;
    });

    public static final Hook<SnowFallHook> SNOW_FALL = HookFactory.createArrayBacked(SnowFallHook.class, callbacks -> (world, pos) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onSnowFall(world, pos))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<BlockStateChangeHook> CAULDRON_PRECIPITATION = HookFactory.createArrayBacked(BlockStateChangeHook.class, callbacks -> (world, pos, newState) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onChange(world, pos, newState))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<BlockStateChangeHook> CAULDRON_DRIP_STONE = HookFactory.createArrayBacked(BlockStateChangeHook.class, callbacks -> (world, pos, newState) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onChange(world, pos, newState))
                cancelled = true;

        return cancelled;
    });

    /**
     * Hook invoked when a coral water check is done.
     */
    public static final Hook<FadeHook> CORAL_DEATH = HookFactory.createArrayBacked(FadeHook.class, callbacks -> (world, pos) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onFade(world, pos))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<TileDropHook> BLOCK_ITEM_DROP = HookFactory.createArrayBacked(TileDropHook.class, callbacks -> (world, pos, stack) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onTileDrop(world, pos, stack))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<TileDropXpHook> BLOCK_XP_DROP = HookFactory.createArrayBacked(TileDropXpHook.class, callbacks -> (world, pos, experience) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onTileDropExperience(world, pos, experience))
                cancelled = true;

        return cancelled;
    });

    public interface ExplosionHook {
        boolean onExplode(Entity exploder);
    }

    public interface FadeHook {
        boolean onFade(World world, BlockPos pos);
    }

    public interface FrostWalkerFreezeHook {
        boolean onFreeze(World world, BlockPos pos, LivingEntity entity);
    }

    public interface SnowFallHook {
        boolean onSnowFall(World world, BlockPos pos);
    }

    public interface BlockStateChangeHook {
        boolean onChange(World world, BlockPos pos, BlockState newState);
    }

    public interface TileDropHook {
        boolean onTileDrop(World world, BlockPos pos, ItemStack stack);
    }

    public interface TileDropXpHook {
        boolean onTileDropExperience(World world, BlockPos pos, int xp);
    }
}
