package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.player.CraftingRecipeCallback;

import java.util.Optional;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin {

    @WrapOperation(
            method = "updateResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/recipe/RecipeManager;getFirstMatch(Lnet/minecraft/recipe/RecipeType;Lnet/minecraft/recipe/input/RecipeInput;Lnet/minecraft/world/World;Lnet/minecraft/recipe/RecipeEntry;)Ljava/util/Optional;"
            )
    )
    private static Optional<RecipeEntry<CraftingRecipe>> kibu$modifyCraftingResult(
            RecipeManager instance, RecipeType<CraftingRecipe> type, RecipeInput _input, World world,
            RecipeEntry<Recipe<CraftingRecipeInput>> recipe, Operation<Optional<RecipeEntry<CraftingRecipe>>> original,
            @Local(argsOnly = true) PlayerEntity player,
            @Local(argsOnly = true) RecipeInputInventory inventory
    ) {

        var input = (CraftingRecipeInput) _input;

        var pending = CraftingRecipeCallback.HOOK.invoker().modifyRecipe(player, instance, type, input, recipe);

        if (pending.isPass()) {
            return original.call(instance, type, input, world, recipe);
        }

        return pending.get();
    }
}
