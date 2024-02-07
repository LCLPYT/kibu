package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
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
                    target = "Lnet/minecraft/recipe/RecipeManager;getFirstMatch(Lnet/minecraft/recipe/RecipeType;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/world/World;)Ljava/util/Optional;"
            )
    )
    private static Optional<RecipeEntry<CraftingRecipe>> kibu$modifyCraftingResult(
            RecipeManager instance, RecipeType<CraftingRecipe> type, Inventory _inventory, World world,
            Operation<Optional<RecipeEntry<CraftingRecipe>>> original, @Local(argsOnly = true) PlayerEntity player
    ) {
        RecipeInputInventory inventory = (RecipeInputInventory) _inventory;

        var pending = CraftingRecipeCallback.HOOK.invoker().modifyRecipe(player, instance, type, inventory, world);

        if (pending.isPass()) {
            return original.call(instance, type, inventory, world);
        }

        return pending.get();
    }
}
