package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import work.lclpnet.kibu.hook.player.CraftingRecipeCallback;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin {

    @ModifyVariable(
            method = "updateResult",
            at = @At(
                    value = "LOAD",
                    ordinal = 0
            ),
            index = 8
    )
    private static ItemStack kibu$modifyCraftingResult(ItemStack result,
                                                  @Local(argsOnly = true) RecipeInputInventory inventory,
                                                  @Local CraftingRecipeInput input,
                                                  @Local ServerPlayerEntity player) {

        var pending = CraftingRecipeCallback.HOOK.invoker().modifyRecipe(player, input, result);

        if (pending.isPass()) {
            return result;
        }

        return pending.get()
                .filter(stack -> stack.isItemEnabled(player.getEntityWorld().getEnabledFeatures()))
                .orElse(ItemStack.EMPTY);
    }
}
