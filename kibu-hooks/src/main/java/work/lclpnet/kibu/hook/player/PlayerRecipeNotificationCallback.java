package work.lclpnet.kibu.hook.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface PlayerRecipeNotificationCallback {

    Hook<PlayerRecipeNotificationCallback> HOOK = HookFactory.createArrayBacked(PlayerRecipeNotificationCallback.class,
            callbacks -> (player, recipeEntry, displayEntry) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onDisplay(player, recipeEntry, displayEntry)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    /**
     * Called for each recipe that is to be displayed to a player when unlocking a recipe.
     * This doesn't include recipes that are not displayed by default.
     * @param player The player that is about to receive the recipe notification.
     * @param recipeEntry The recipe entry.
     * @param displayEntry The recipe display entry.
     * @return True, if the recipe should be hidden, false for default behaviour.
     */
    boolean onDisplay(ServerPlayer player, RecipeHolder<?> recipeEntry, RecipeDisplayEntry displayEntry);
}
