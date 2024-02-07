package work.lclpnet.kibu.hook.util;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class RecipeUtils {

    private RecipeUtils() {}

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> Optional<RecipeEntry<T>> getRecipe(RecipeManager recipeManager, Identifier id, RecipeType<T> type) {
        return recipeManager.get(id)
                .filter(recipeEntry -> type == recipeEntry.value().getType())
                .map(recipeEntry -> (RecipeEntry<T>) recipeEntry);
    }
}
