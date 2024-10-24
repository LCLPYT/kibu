package work.lclpnet.kibu.hook.player;

import net.minecraft.recipe.RecipeDisplayEntry;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.server.network.ServerPlayerEntity;
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
    boolean onDisplay(ServerPlayerEntity player, RecipeEntry<?> recipeEntry, RecipeDisplayEntry displayEntry);
}
