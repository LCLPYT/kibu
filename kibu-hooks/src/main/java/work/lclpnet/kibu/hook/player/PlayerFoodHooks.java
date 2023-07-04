package work.lclpnet.kibu.hook.player;

import net.minecraft.entity.player.PlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public class PlayerFoodHooks {

    private PlayerFoodHooks() {}

    public static final Hook<FoodFloatLevel> SATURATION_CHANGE = HookFactory.createArrayBacked(FoodFloatLevel.class, callbacks -> (player, fromLevel, toLevel) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onChange(player, fromLevel, toLevel))
                cancelled = true;

        return cancelled;
    });
    public static final Hook<FoodFloatLevel> EXHAUSTION_CHANGE = HookFactory.createArrayBacked(FoodFloatLevel.class, callbacks -> (player, fromLevel, toLevel) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onChange(player, fromLevel, toLevel))
                cancelled = true;

        return cancelled;
    });
    public static final Hook<FoodIntLevel> LEVEL_CHANGE = HookFactory.createArrayBacked(FoodIntLevel.class, callbacks -> (player, fromLevel, toLevel) -> {
        boolean cancelled = false;

        for (var callback : callbacks)
            if (callback.onChange(player, fromLevel, toLevel))
                cancelled = true;

        return cancelled;
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
