package work.lclpnet.kibu.hook.player;

import net.minecraft.entity.player.PlayerEntity;
import work.lclpnet.mplugins.hook.Hook;
import work.lclpnet.mplugins.hook.HookFactory;

public class FoodHooks {

    public static final Hook<FoodFloatLevel> SATURATION_CHANGE = HookFactory.createArrayBacked(FoodFloatLevel.class, callbacks -> (player, fromLevel, toLevel) -> {
        for (FoodFloatLevel callback : callbacks)
            if (callback.onChange(player, fromLevel, toLevel))
                return true;

        return false;
    });

    public static final Hook<FoodFloatLevel> EXHAUSTION_CHANGE = HookFactory.createArrayBacked(FoodFloatLevel.class, callbacks -> (player, fromLevel, toLevel) -> {
        for (FoodFloatLevel callback : callbacks)
            if (callback.onChange(player, fromLevel, toLevel))
                return true;

        return false;
    });

    public static final Hook<FoodIntLevel> LEVEL_CHANGE = HookFactory.createArrayBacked(FoodIntLevel.class, callbacks -> (player, fromLevel, toLevel) -> {
        for (FoodIntLevel callback : callbacks)
            if (callback.onChange(player, fromLevel, toLevel))
                return true;

        return false;
    });

    public interface FoodFloatLevel {
        /**
         * Called when a food float value changes.
         *
         * @param player The player on that the event occurs.
         * @param fromLevel The old food level.
         * @param toLevel The new food level.
         * @return True, if the food level change should be cancelled.
         */
        boolean onChange(PlayerEntity player, float fromLevel, float toLevel);
    }

    public interface FoodIntLevel {
        /**
         * Called when a food integer value changes.
         *
         * @param player The player on that the event occurs.
         * @param fromLevel The old food level.
         * @param toLevel The new food level.
         * @return True, if the food level change should be cancelled.
         */
        boolean onChange(PlayerEntity player, int fromLevel, int toLevel);
    }
}
