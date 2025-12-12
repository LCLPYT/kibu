package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
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
    private static final ThreadLocal<ServerPlayer> playerRef = ThreadLocal.withInitial(() -> null);

    @Inject(
            method = "addRecipes",
            at = @At("HEAD")
    )
    public void kibu$beforeUnlockRecipes(Collection<RecipeHolder<?>> recipes, ServerPlayer player, CallbackInfoReturnable<Integer> cir) {
        playerRef.set(player);  // player is needed for notification adjustment, as it is not available in its lambda context
    }

    @ModifyArg(
            method = "method_64591",  // this is a lambda in unlockRecipes(); naming may change
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket$Entry;<init>(Lnet/minecraft/world/item/crafting/display/RecipeDisplayEntry;ZZ)V"
            ),
            index = 1
    )
    private static boolean kibu$adjustRecipeNotification(boolean showNotification,
                                                         @Local(argsOnly = true) RecipeHolder<?> recipeRef,
                                                         @Local(argsOnly = true) RecipeDisplayEntry displayEntry) {
        if (!showNotification) return false;

        ServerPlayer player = playerRef.get();

        if (player == null) return true;

        boolean hide = PlayerRecipeNotificationCallback.HOOK.invoker().onDisplay(player, recipeRef, displayEntry);

        return !hide;
    }
}
