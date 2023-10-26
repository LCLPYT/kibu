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
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.player.PlayerConnectionHooks;
import work.lclpnet.kibu.hook.player.PlayerSpawnLocationCallback;

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
    public void kibu$sendJoinMessage(PlayerManager instance, Text message, boolean overlay) {
        // ignore default join message
    }

    @Inject(
            method = "onPlayerConnect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Z)V"
            )
    )
    public void kibu$sendCustomJoinMessage(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        GameProfile gameprofile = player.getGameProfile();
        UserCache userCache = this.server.getUserCache();

        if (userCache == null) return;

        GameProfile byUuid = userCache.getByUuid(gameprofile.getId()).orElse(null);
        String s = byUuid == null ? gameprofile.getName() : byUuid.getName();

        final MutableText originalText;
        if (player.getGameProfile().getName().equalsIgnoreCase(s)) {
            originalText = Text.translatable("multiplayer.player.joined", player.getDisplayName());
        } else {
            originalText = Text.translatable("multiplayer.player.joined.renamed", player.getDisplayName(), s);
        }

        Text text = PlayerConnectionHooks.JOIN_MESSAGE.invoker().onJoin(player, originalText.formatted(Formatting.YELLOW));
        if (text != null) {
            this.broadcast(text, false);
            player.sendMessage(text, false);
        }

        PlayerConnectionHooks.JOIN.invoker().act(player);
    }

    @Inject(
            method = "onPlayerConnect",
            at = @At("TAIL")
    )
    public void kibu$afterConnected(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        var data = new PlayerSpawnLocationCallback.LocationData(player, true, player.getServerWorld(),
                player.getPos(), player.getYaw(), player.getPitch());

        PlayerSpawnLocationCallback.HOOK.invoker().onSpawn(data);

        if (data.isDirty()) {
            Vec3d pos = data.getPosition();
            player.teleport(data.getWorld(), pos.getX(), pos.getY(), pos.getZ(), data.getYaw(), data.getPitch());
        }
    }

    @Inject(
            method = "respawnPlayer",
            at = @At("RETURN")
    )
    public void kibu$afterRespawn(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        ServerPlayerEntity player = cir.getReturnValue();

        var data = new PlayerSpawnLocationCallback.LocationData(player, false, player.getServerWorld(),
                player.getPos(), player.getYaw(), player.getPitch());

        PlayerSpawnLocationCallback.HOOK.invoker().onSpawn(data);

        if (data.isDirty()) {
            Vec3d pos = data.getPosition();
            player.teleport(data.getWorld(), pos.getX(), pos.getY(), pos.getZ(), data.getYaw(), data.getPitch());
        }
    }
}
