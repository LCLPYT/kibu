package work.lclpnet.kibu.hook.util;

import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PendingRecipe {

    private static final PendingRecipe PASS = new PendingRecipe(null);
    private static final PendingRecipe EMPTY = new PendingRecipe(null);
    @Nullable
    private final RecipeEntry<CraftingRecipe> entry;

    private PendingRecipe(@Nullable RecipeEntry<CraftingRecipe> entry) {
        this.entry = entry;
    }

    public Optional<RecipeEntry<CraftingRecipe>> get() {
        return Optional.ofNullable(entry);
    }

    public boolean isPass() {
        return PASS == this;
    }

    public static PendingRecipe empty() {
        return EMPTY;
    }

    public static PendingRecipe pass() {
        return PASS;
    }

    public static PendingRecipe of(@Nullable RecipeEntry<CraftingRecipe> entry) {
        if (entry == null) {
            return empty();
        }

        return new PendingRecipe(entry);
    }
}
