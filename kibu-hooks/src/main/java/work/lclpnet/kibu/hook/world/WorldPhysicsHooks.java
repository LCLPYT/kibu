package work.lclpnet.kibu.hook.world;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldPhysicsHooks {

    public static final Event<ExplosionHook> EXPLOSION = EventFactory.createArrayBacked(ExplosionHook.class, callbacks -> exploder -> {
        for (ExplosionHook callback : callbacks)
            if (callback.onExplode(exploder))
                return true;

        return false;
    });

    public static final Event<FadeHook> MELT = EventFactory.createArrayBacked(FadeHook.class, callbacks -> (world, pos) -> {
        for (FadeHook callback : callbacks)
            if (callback.onFade(world, pos))
                return true;

        return false;
    });

    public static final Event<FrostWalkerFreezeHook> FROST_WALKER_FREEZE = EventFactory.createArrayBacked(FrostWalkerFreezeHook.class, callbacks -> (world, pos, entity) -> {
        for (FrostWalkerFreezeHook callback : callbacks)
            if (callback.onFreeze(world, pos, entity))
                return true;

        return false;
    });

    public static final Event<FadeHook> FREEZE = EventFactory.createArrayBacked(FadeHook.class, callbacks -> (world, pos) -> {
        for (FadeHook callback : callbacks)
            if (callback.onFade(world, pos))
                return true;

        return false;
    });

    public static final Event<SnowFallHook> SNOW_FALL = EventFactory.createArrayBacked(SnowFallHook.class, callbacks -> (world, pos) -> {
        for (SnowFallHook callback : callbacks)
            if (callback.onSnowFall(world, pos))
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
}
