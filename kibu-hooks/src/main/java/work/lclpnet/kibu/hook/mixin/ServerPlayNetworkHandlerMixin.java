package work.lclpnet.kibu.hook.mixin;

import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
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
import work.lclpnet.kibu.hook.player.PlayerConnectionHooks;
import work.lclpnet.kibu.hook.player.PlayerInventoryHooks;

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
    public void kibu$sendQuitMessage(PlayerManager instance, Text message, boolean overlay) {
        Text text = PlayerConnectionHooks.QUIT_MESSAGE.invoker().onQuit(player, message);

        if (text != null) {
            instance.broadcast(text, overlay);
        }
    }

    @Inject(
            method = "onUpdateSelectedSlot",
            at = @At("TAIL")
    )
    public void kibu$onUpdateSelectedSlot(UpdateSelectedSlotC2SPacket packet, CallbackInfo ci) {
        PlayerInventoryHooks.SLOT_CHANGE.invoker().onChangeSlot(player, packet.getSelectedSlot());
    }

    @Inject(
            method = "onPlayerAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;",
                    ordinal = 0
            ),
            cancellable = true
    )
    public void kibu$beforeSwapHands(PlayerActionC2SPacket packet, CallbackInfo ci) {
        boolean cancel = PlayerInventoryHooks.SWAP_HANDS.invoker().onSwapHands(player, player.getInventory().selectedSlot);
        if (cancel) {
            ci.cancel();
        }
    }

    @Inject(
            method = "onPlayerAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;clearActiveItem()V"
            )
    )
    public void kibu$afterSwapHands(PlayerActionC2SPacket packet, CallbackInfo ci) {
        PlayerInventoryHooks.SWAPPED_HANDS.invoker().onSwappedHands(player, player.getInventory().selectedSlot);
    }

    @Inject(
            method = "onClickSlot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/packet/c2s/play/ClickSlotC2SPacket;getSlot()I"
            ),
            cancellable = true
    )
    public void kibu$onClickSlot(ClickSlotC2SPacket packet, CallbackInfo ci) {
        var event = new PlayerInventoryHooks.ClickEvent(player, packet.getSlot(), packet.getButton(), packet.getStack(),
                packet.getActionType(), packet.getModifiedStacks());

        boolean cancel = PlayerInventoryHooks.MODIFY_INVENTORY.invoker().onModify(event);
        if (cancel) {
            ci.cancel();
            this.player.currentScreenHandler.syncState();
        }
    }

    @Inject(
            method = "onClickSlot",
            at = @At("TAIL")
    )
    public void kibu$onClickedSlot(ClickSlotC2SPacket packet, CallbackInfo ci) {
        var event = new PlayerInventoryHooks.ClickEvent(player, packet.getSlot(), packet.getButton(), packet.getStack(),
                packet.getActionType(), packet.getModifiedStacks());

        PlayerInventoryHooks.MODIFIED_INVENTORY.invoker().onModified(event);
    }

    @Inject(
            method = "onCreativeInventoryAction",
            at = @At("TAIL")
    )
    public void kibu$onCreativeClickedSlot(CreativeInventoryActionC2SPacket packet, CallbackInfo ci) {
        var event = new PlayerInventoryHooks.CreativeClickEvent(player, packet.getSlot(), packet.getItemStack());

        PlayerInventoryHooks.MODIFIED_CREATIVE_INVENTORY.invoker().onModified(event);
    }
}
