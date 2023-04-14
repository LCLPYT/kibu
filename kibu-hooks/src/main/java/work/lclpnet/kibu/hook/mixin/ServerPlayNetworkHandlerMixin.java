package work.lclpnet.kibu.hook.mixin;

import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.player.PlayerHooks;
import work.lclpnet.kibu.hook.player.PlayerSlotChangeHook;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Shadow public ServerPlayerEntity player;

    @Redirect(
            method = "onDisconnected",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Z)V"
            )
    )
    public void sendQuitMessage(PlayerManager instance, Text message, boolean overlay) {
        Text text = PlayerHooks.QUIT.invoker().onQuit(player, message);

        if (text != null) {
            instance.broadcast(text, overlay);
        }
    }

    @Inject(
            method = "onUpdateSelectedSlot",
            at = @At("TAIL")
    )
    public void illwalls$onUpdateSelectedSlot(UpdateSelectedSlotC2SPacket packet, CallbackInfo ci) {
        PlayerSlotChangeHook.HOOK.invoker().onChangeSlot(packet.getSelectedSlot());
    }
}
