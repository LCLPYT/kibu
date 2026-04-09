package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import work.lclpnet.kibu.hook.player.CraftingRecipeCallback;

@Mixin(CraftingMenu.class)
public class CraftingMenuMixin {

    @ModifyVariable(
            method = "slotChangedCraftingGrid",
            at = @At(
                    value = "LOAD",
                    ordinal = 0
            ),
            name = "result"
    )
    private static ItemStack kibu$modifyCraftingResult(ItemStack result,
                                                       @Local(name = "input") CraftingInput input,
                                                       @Local(name = "serverPlayer") ServerPlayer serverPlayer) {

        var pending = CraftingRecipeCallback.HOOK.invoker().modifyRecipe(serverPlayer, input, result);

        if (pending.isPass()) {
            return result;
        }

        return pending.get()
                .filter(stack -> stack.isItemEnabled(serverPlayer.level().enabledFeatures()))
                .orElse(ItemStack.EMPTY);
    }
}
