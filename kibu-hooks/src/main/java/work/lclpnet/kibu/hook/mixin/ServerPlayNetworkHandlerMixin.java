package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import work.lclpnet.kibu.hook.player.PlayerConnectionHooks;
import work.lclpnet.kibu.hook.player.PlayerInventoryHooks;
import work.lclpnet.kibu.hook.player.PlayerMoveCallback;
import work.lclpnet.kibu.hook.player.PlayerToggleFlightCallback;
import work.lclpnet.kibu.hook.util.PositionRotation;

import java.util.Set;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow public ServerPlayerEntity player;

    @Shadow
    private static double clampHorizontal(double d) {
        throw new AssertionError();
    }

    @Shadow
    private static double clampVertical(double d) {
        throw new AssertionError();
    }

    @Shadow public abstract void requestTeleport(double x, double y, double z, float yaw, float pitch, Set<PositionFlag> set);

    @Unique
    private double lastX = Double.NaN, lastY = Double.NaN, lastZ = Double.NaN;
    @Unique
    private float lastYaw = Float.NaN, lastPitch = Float.NaN;
    @Unique
    private boolean teleporting = false;
    @Unique
    private double modifiedVelocityY = Double.NaN;

    @Redirect(
            method = "cleanUp",
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
            method = "onDisconnected",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerCommonNetworkHandler;onDisconnected(Lnet/minecraft/text/Text;)V"
            )
    )
    public void kibu$afterQuit(Text reason, CallbackInfo ci) {
        PlayerConnectionHooks.QUIT.invoker().act(player);
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
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/PlayerScreenHandler;getSlot(I)Lnet/minecraft/screen/slot/Slot;"
            )
    )
    public void kibu$onCreativeClickSlot(CreativeInventoryActionC2SPacket packet, CallbackInfo ci) {
        var event = new PlayerInventoryHooks.CreativeClickEvent(player, packet.getSlot(), packet.getStack());

        PlayerInventoryHooks.MODIFY_CREATIVE_INVENTORY.invoker().onModify(event);
    }

    @Inject(
            method = "onCreativeInventoryAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/PlayerScreenHandler;sendContentUpdates()V",
                    shift = At.Shift.AFTER
            )
    )
    public void kibu$onCreativeClickedSlot(CreativeInventoryActionC2SPacket packet, CallbackInfo ci) {
        var event = new PlayerInventoryHooks.CreativeClickEvent(player, packet.getSlot(), packet.getStack());

        PlayerInventoryHooks.MODIFIED_CREATIVE_INVENTORY.invoker().onModified(event);
    }

    @Inject(
            method = "onPlayerMove",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;getBoundingBox()Lnet/minecraft/util/math/Box;"
            ),
            cancellable = true
    )
    public void kibu$preMove(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        if (Double.isNaN(lastX)) {
            lastX = player.getX();
            lastY = player.getY();
            lastZ = player.getZ();
            lastYaw = player.getYaw();
            lastPitch = player.getPitch();
        }

        double toX = clampHorizontal(packet.getX(lastX));
        double toY = clampVertical(packet.getY(lastY));
        double toZ = clampHorizontal(packet.getZ(lastZ));
        float toYaw = MathHelper.wrapDegrees(packet.getYaw(lastYaw));
        float toPitch = MathHelper.wrapDegrees(packet.getPitch(lastPitch));

        // check angle against last recorded position to debounce movement hook
        double distance = Math.pow(toX - lastX, 2) + Math.pow(toY - lastY, 2) + Math.pow(toZ - lastZ, 2);
        float angle = Math.abs(toYaw - lastYaw) + Math.abs(toPitch - lastPitch);

        if (!(distance >= 0.00390625) && !(angle >= 0.01f)) return;

        PositionRotation from = new PositionRotation(lastX, lastY, lastZ, lastYaw, lastPitch);
        PositionRotation to = new PositionRotation(toX, toY, toZ, toYaw, toPitch);

        teleporting = false;

        double motionY = player.getVelocity().getY();

        if (PlayerMoveCallback.HOOK.invoker().onMove(player, from, to)) {
            // movement disallowed; reset
            requestTeleport(from.getX(), from.getY(), from.getZ(), from.getYaw(), from.getPitch(), Set.of());

            ci.cancel();
            return;
        }

        // the player was teleported by a hook
        if (teleporting) {
            ci.cancel();
            return;
        }

        // in case a hook modified the y-velocity and the hook was not cancelled
        double newMotionY = player.getVelocity().getY();

        if (Math.abs(newMotionY - motionY) > 1e-9d) {
            modifiedVelocityY = newMotionY;
        }

        lastX = toX;
        lastY = toY;
        lastZ = toZ;
        lastYaw = toYaw;
        lastPitch = toPitch;
    }

    @Inject(
            method = "onPlayerMove",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;jump()V",
                    shift = At.Shift.AFTER
            )
    )
    public void kibu$onJump(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        if (Double.isNaN(modifiedVelocityY)) return;

        Vec3d velocity = player.getVelocity();
        player.setVelocity(velocity.getX(), modifiedVelocityY, velocity.getZ());
    }

    @Inject(
            method = "onPlayerMove",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"
            )
    )
    public void kibu$resetModifiedVelocityY(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        modifiedVelocityY = Double.NaN;
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(
            method = "requestTeleport(DDDFFLjava/util/Set;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;requestedTeleportPos:Lnet/minecraft/util/math/Vec3d;"
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    public void kibu$onRequestTeleport(double _x, double _y, double _z, float _yaw, float _pitch, Set<PositionFlag> set, CallbackInfo ci,
                                       double x, double y, double z, float yaw, float pitch) {
        teleporting = true;
        lastX = x;
        lastY = y;
        lastZ = z;
        lastYaw = yaw;
        lastPitch = pitch;
    }

    @Inject(
            method = "onUpdatePlayerAbilities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/packet/c2s/play/UpdatePlayerAbilitiesC2SPacket;isFlying()Z"
            ),
            cancellable = true
    )
    public void kibu$onToggleFlight(UpdatePlayerAbilitiesC2SPacket packet, CallbackInfo ci) {
        PlayerAbilities abilities = player.getAbilities();

        if (!abilities.allowFlying) return;

        boolean fly = packet.isFlying();

        if (abilities.flying == fly) return;

        if (PlayerToggleFlightCallback.HOOK.invoker().onToggleFlight(player, fly)) {
            ci.cancel();
            player.sendAbilitiesUpdate();
        }
    }
}
