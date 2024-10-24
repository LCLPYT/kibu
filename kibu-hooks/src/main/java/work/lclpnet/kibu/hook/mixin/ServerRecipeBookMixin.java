package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.recipe.RecipeDisplayEntry;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerRecipeBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.player.PlayerRecipeNotificationCallback;

import java.util.Collection;

@Mixin(ServerRecipeBook.class)
public class ServerRecipeBookMixin {

    @Unique
    private static final ThreadLocal<ServerPlayerEntity> playerRef = ThreadLocal.withInitial(() -> null);

    @Inject(
            method = "unlockRecipes",
            at = @At("HEAD")
    )
    public void kibu$beforeUnlockRecipes(Collection<RecipeEntry<?>> recipes, ServerPlayerEntity player, CallbackInfoReturnable<Integer> cir) {
        playerRef.set(player);  // player is needed for notification adjustment, as it is not available in its lambda context
    }

    @ModifyArg(
            method = "method_64591",  // this is a lambda in unlockRecipes(); naming may change
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/packet/s2c/play/RecipeBookAddS2CPacket$Entry;<init>(Lnet/minecraft/recipe/RecipeDisplayEntry;ZZ)V"
            ),
            index = 1
    )
    private static boolean kibu$adjustRecipeNotification(boolean showNotification,
                                                         @Local(argsOnly = true) RecipeEntry<?> recipeRef,
                                                         @Local(argsOnly = true) RecipeDisplayEntry displayEntry) {
        if (!showNotification) return false;

        ServerPlayerEntity player = playerRef.get();

        if (player == null) return true;

        boolean hide = PlayerRecipeNotificationCallback.HOOK.invoker().onDisplay(player, recipeRef, displayEntry);

        return !hide;
    }
}
