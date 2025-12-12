package work.lclpnet.kibu.hook.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.players.UserNameToIdResolver;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
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

import java.util.Set;

@Mixin(PlayerList.class)
public abstract class PlayerManagerMixin {

    @Shadow @Final private MinecraftServer server;

    @Shadow public abstract void broadcastSystemMessage(Component message, boolean overlay);

    @Redirect(
            method = "placeNewPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
            )
    )
    public void kibu$sendJoinMessage(PlayerList instance, Component message, boolean overlay) {
        // ignore default join message
    }

    @Inject(
            method = "placeNewPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
            )
    )
    public void kibu$sendCustomJoinMessage(Connection connection, ServerPlayer player, CommonListenerCookie clientData, CallbackInfo ci) {
        NameAndId configEntry = player.nameAndId();
        UserNameToIdResolver nameToIdCache = this.server.services().nameToIdCache();

        if (nameToIdCache == null) return;

        NameAndId byUuid = nameToIdCache.get(configEntry.id()).orElse(null);
        String s = byUuid == null ? configEntry.name() : byUuid.name();

        final MutableComponent originalText;
        if (player.getGameProfile().name().equalsIgnoreCase(s)) {
            originalText = Component.translatable("multiplayer.player.joined", player.getDisplayName());
        } else {
            originalText = Component.translatable("multiplayer.player.joined.renamed", player.getDisplayName(), s);
        }

        Component text = PlayerConnectionHooks.JOIN_MESSAGE.invoker().onJoin(player, originalText.withStyle(ChatFormatting.YELLOW));
        if (text != null) {
            this.broadcastSystemMessage(text, false);
            player.displayClientMessage(text, false);
        }
    }

    @Inject(
            method = "placeNewPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;initInventoryMenu()V",
                    shift = At.Shift.AFTER
            )
    )
    public void kibu$afterConnected(Connection connection, ServerPlayer player, CommonListenerCookie clientData, CallbackInfo ci) {
        PlayerConnectionHooks.JOIN.invoker().act(player);

        var data = new PlayerSpawnLocationCallback.LocationData(player, true, player.level(),
                player.position(), player.getYRot(), player.getXRot());

        PlayerSpawnLocationCallback.HOOK.invoker().onSpawn(data);

        if (data.isDirty()) {
            Vec3 pos = data.getPosition();
            player.teleportTo(data.getWorld(), pos.x(), pos.y(), pos.z(), Set.of(), data.getYaw(), data.getPitch(), true);
        }
    }

    @Inject(
            method = "respawn",
            at = @At("RETURN")
    )
    public void kibu$afterRespawn(ServerPlayer oldPlayer, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> cir) {
        ServerPlayer player = cir.getReturnValue();

        var data = new PlayerSpawnLocationCallback.LocationData(player, false, player.level(),
                player.position(), player.getYRot(), player.getXRot());

        PlayerSpawnLocationCallback.HOOK.invoker().onSpawn(data);

        if (data.isDirty()) {
            Vec3 pos = data.getPosition();
            player.teleportTo(data.getWorld(), pos.x(), pos.y(), pos.z(), Set.of(), data.getYaw(), data.getPitch(), true);
        }
    }
}
