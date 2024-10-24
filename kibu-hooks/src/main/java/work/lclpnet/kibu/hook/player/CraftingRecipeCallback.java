package work.lclpnet.kibu.hook.player;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;
import work.lclpnet.kibu.hook.util.PendingResult;

public interface CraftingRecipeCallback {

    Hook<CraftingRecipeCallback> HOOK = HookFactory.createArrayBacked(CraftingRecipeCallback.class, callbacks
            -> (player, input, result) -> {

        for (CraftingRecipeCallback callback : callbacks) {
            var pending = callback.modifyRecipe(player, input, result);

            if (pending.isPass()) continue;

            return pending;
        }

        return PendingResult.pass();
    });

    PendingResult<ItemStack> modifyRecipe(ServerPlayerEntity player, CraftingRecipeInput input, ItemStack result);
}
