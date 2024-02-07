package work.lclpnet.kibu.hook.player;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.world.World;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;
import work.lclpnet.kibu.hook.util.PendingRecipe;

public interface CraftingRecipeCallback {

    Hook<CraftingRecipeCallback> HOOK = HookFactory.createArrayBacked(CraftingRecipeCallback.class, callbacks
            -> (recipeManager, type, inventory, world) -> {

        for (CraftingRecipeCallback callback : callbacks) {
            var pending = callback.modifyRecipe(recipeManager, type, inventory, world);

            if (pending.isPass()) continue;

            return pending;
        }

        return PendingRecipe.pass();
    });

    PendingRecipe modifyRecipe(RecipeManager recipeManager, RecipeType<CraftingRecipe> type,
                               RecipeInputInventory inventory, World world);
}
