package work.lclpnet.kibu.hook.world;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public class WorldPhysicsHooks {

    public static final Hook<ExplosionHook> EXPLOSION = HookFactory.createArrayBacked(ExplosionHook.class, callbacks -> exploder -> {
        for (var callback : callbacks)
            if (callback.onExplode(exploder))
                return true;

        return false;
    });

    public static final Hook<FadeHook> MELT = HookFactory.createArrayBacked(FadeHook.class, callbacks -> (world, pos) -> {
        for (var callback : callbacks)
            if (callback.onFade(world, pos))
                return true;

        return false;
    });

    public static final Hook<FrostWalkerFreezeHook> FROST_WALKER_FREEZE = HookFactory.createArrayBacked(FrostWalkerFreezeHook.class, callbacks -> (world, pos, entity) -> {
        for (var callback : callbacks)
            if (callback.onFreeze(world, pos, entity))
                return true;

        return false;
    });

    public static final Hook<FadeHook> FREEZE = HookFactory.createArrayBacked(FadeHook.class, callbacks -> (world, pos) -> {
        for (var callback : callbacks)
            if (callback.onFade(world, pos))
                return true;

        return false;
    });

    public static final Hook<SnowFallHook> SNOW_FALL = HookFactory.createArrayBacked(SnowFallHook.class, callbacks -> (world, pos) -> {
        for (var callback : callbacks)
            if (callback.onSnowFall(world, pos))
                return true;

        return false;
    });

    public static final Hook<BlockStateChangeHook> CAULDRON_PRECIPITATION = HookFactory.createArrayBacked(BlockStateChangeHook.class, callbacks -> (world, pos, newState) -> {
        for (var callback : callbacks)
            if (callback.onChange(world, pos, newState))
                return true;

        return false;
    });

    public static final Hook<BlockStateChangeHook> CAULDRON_DRIP_STONE = HookFactory.createArrayBacked(BlockStateChangeHook.class, callbacks -> (world, pos, newState) -> {
        for (var callback : callbacks)
            if (callback.onChange(world, pos, newState))
                return true;

        return false;
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
}
