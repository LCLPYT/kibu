package work.lclpnet.kibu.hook.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public class LevelPhysicsHooks {

    public static final Hook<ExplosionHook> EXPLOSION = HookFactory.createArrayBacked(ExplosionHook.class, callbacks -> explosion -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onExplode(explosion))
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

    public static final Hook<ReplaceDiskEnchantmentHook> REPLACE_DISK_ENCHANTMENT = HookFactory.createArrayBacked(ReplaceDiskEnchantmentHook.class, callbacks -> (world, pos, entity, state) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onApply(world, pos, entity, state))
                cancelled = true;

        return cancelled;
    });

    public static final Hook<FadeHook> FREEZE = HookFactory.createArrayBacked(FadeHook.class, callbacks -> (world, pos) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onFade(world, pos))
                cancelled = true;

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
        boolean onExplode(ServerExplosion explosion);
    }

    public interface FadeHook {
        boolean onFade(Level world, BlockPos pos);
    }

    public interface ReplaceDiskEnchantmentHook {
        boolean onApply(Level world, BlockPos pos, @Nullable LivingEntity entity, BlockState state);
    }

    public interface SnowFallHook {
        boolean onSnowFall(Level world, BlockPos pos);
    }

    public interface BlockStateChangeHook {
        boolean onChange(Level world, BlockPos pos, BlockState newState);
    }

    public interface TileDropHook {
        boolean onTileDrop(Level world, BlockPos pos, ItemStack stack);
    }

    public interface TileDropXpHook {
        boolean onTileDropExperience(Level world, BlockPos pos, int xp);
    }
}
