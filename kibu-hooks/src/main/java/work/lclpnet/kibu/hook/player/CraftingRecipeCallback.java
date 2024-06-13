package work.lclpnet.kibu.hook.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.CraftingRecipeInput;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;
import work.lclpnet.kibu.hook.util.PendingRecipe;

public interface CraftingRecipeCallback {

    Hook<CraftingRecipeCallback> HOOK = HookFactory.createArrayBacked(CraftingRecipeCallback.class, callbacks
            -> (player, recipeManager, type, input, cached) -> {

        for (CraftingRecipeCallback callback : callbacks) {
            var pending = callback.modifyRecipe(player, recipeManager, type, input, cached);

            if (pending.isPass()) continue;

            return pending;
        }

        return PendingRecipe.pass();
    });

    PendingRecipe modifyRecipe(PlayerEntity player, RecipeManager recipeManager, RecipeType<CraftingRecipe> type,
                               CraftingRecipeInput input, RecipeEntry<Recipe<CraftingRecipeInput>> cached);
}
