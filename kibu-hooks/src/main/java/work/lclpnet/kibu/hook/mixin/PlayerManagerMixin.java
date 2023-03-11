package work.lclpnet.kibu.hook.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.UserCache;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.player.PlayerHooks;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Shadow @Final private MinecraftServer server;

    @Shadow public abstract void broadcast(Text message, boolean overlay);

    @Redirect(
            method = "onPlayerConnect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Z)V"
            )
    )
    public void sendJoinMessage(PlayerManager instance, Text message, boolean overlay) {
        // ignore default join message
    }

    @Inject(
            method = "onPlayerConnect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Z)V"
            )
    )
    public void sendCustomJoinMessage(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        GameProfile gameprofile = player.getGameProfile();
        UserCache userCache = this.server.getUserCache();
        GameProfile byUuid = userCache.getByUuid(gameprofile.getId()).orElse(null);
        String s = byUuid == null ? gameprofile.getName() : byUuid.getName();

        final MutableText originalText;
        if (player.getGameProfile().getName().equalsIgnoreCase(s)) {
            originalText = Text.translatable("multiplayer.player.joined", player.getDisplayName());
        } else {
            originalText = Text.translatable("multiplayer.player.joined.renamed", player.getDisplayName(), s);
        }

        Text text = PlayerHooks.JOIN.invoker().onJoin(player, originalText.formatted(Formatting.YELLOW));
        if (text != null) {
            this.broadcast(text, false);
            player.sendMessage(text, false);
        }
    }
}
