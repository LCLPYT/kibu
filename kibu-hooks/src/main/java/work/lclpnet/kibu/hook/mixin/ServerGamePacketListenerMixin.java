package work.lclpnet.kibu.hook.mixin;

import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.player.*;
import work.lclpnet.kibu.hook.util.PlayerUtils;
import work.lclpnet.kibu.hook.util.PositionRotation;

import java.util.Set;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerMixin {

    @Shadow public ServerPlayer player;

    @Shadow
    private static double clampHorizontal(double d) {
        throw new AssertionError();
    }

    @Shadow
    private static double clampVertical(double d) {
        throw new AssertionError();
    }

    @Shadow public abstract void teleport(double x, double y, double z, float yaw, float pitch);

    @Unique
    private double lastX = Double.NaN, lastY = Double.NaN, lastZ = Double.NaN;
    @Unique
    private float lastYaw = Float.NaN, lastPitch = Float.NaN;
    @Unique
    private boolean hookTeleported = false, teleporting = false;
    @Unique
    private double modifiedVelocityY = Double.NaN;

    @Redirect(
            method = "removePlayerFromWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
            )
    )
    public void kibu$sendQuitMessage(PlayerList instance, Component message, boolean overlay) {
        Component text = PlayerConnectionHooks.QUIT_MESSAGE.invoker().onQuit(player, message);

        if (text != null) {
            instance.broadcastSystemMessage(text, overlay);
        }
    }

    @Inject(
            method = "onDisconnect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerCommonPacketListenerImpl;onDisconnect(Lnet/minecraft/network/DisconnectionDetails;)V"
            )
    )
    public void kibu$afterQuit(DisconnectionDetails info, CallbackInfo ci) {
        PlayerConnectionHooks.QUIT.invoker().act(player);
    }

    @Inject(
            method = "handleSetCarriedItem",
            at = @At("TAIL")
    )
    public void kibu$onUpdateSelectedSlot(ServerboundSetCarriedItemPacket packet, CallbackInfo ci) {
        PlayerInventoryHooks.SLOT_CHANGE.invoker().onChangeSlot(player, packet.getSlot());
    }

    @Inject(
            method = "handlePlayerAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;",
                    ordinal = 0
            ),
            cancellable = true
    )
    public void kibu$beforeSwapHands(ServerboundPlayerActionPacket packet, CallbackInfo ci) {
        boolean cancel = PlayerInventoryHooks.SWAP_HANDS.invoker().onSwapHands(player, player.getInventory().getSelectedSlot());
        if (cancel) {
            ci.cancel();
        }
    }

    @Inject(
            method = "handlePlayerAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;stopUsingItem()V"
            )
    )
    public void kibu$afterSwapHands(ServerboundPlayerActionPacket packet, CallbackInfo ci) {
        PlayerInventoryHooks.SWAPPED_HANDS.invoker().onSwappedHands(player, player.getInventory().getSelectedSlot());
    }

    @Inject(
            method = "handleContainerClick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/game/ServerboundContainerClickPacket;slotNum()S"
            ),
            cancellable = true
    )
    public void kibu$onClickSlot(ServerboundContainerClickPacket packet, CallbackInfo ci) {
        var event = new PlayerInventoryHooks.ClickEvent(player, packet.slotNum(), packet.buttonNum(), packet.carriedItem(),
                packet.clickType(), packet.changedSlots());

        boolean cancel = PlayerInventoryHooks.MODIFY_INVENTORY.invoker().onModify(event);
        if (cancel) {
            ci.cancel();
            this.player.containerMenu.sendAllDataToRemote();
            PlayerUtils.syncPlayerItems(player);
        }
    }

    @Inject(
            method = "handleContainerClick",
            at = @At("TAIL")
    )
    public void kibu$onClickedSlot(ServerboundContainerClickPacket packet, CallbackInfo ci) {
        var event = new PlayerInventoryHooks.ClickEvent(player, packet.slotNum(), packet.buttonNum(), packet.carriedItem(),
                packet.clickType(), packet.changedSlots());

        PlayerInventoryHooks.MODIFIED_INVENTORY.invoker().onModified(event);
    }

    @Inject(
            method = "handleSetCreativeModeSlot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/InventoryMenu;getSlot(I)Lnet/minecraft/world/inventory/Slot;"
            )
    )
    public void kibu$onCreativeClickSlot(ServerboundSetCreativeModeSlotPacket packet, CallbackInfo ci) {
        var event = new PlayerInventoryHooks.CreativeClickEvent(player, packet.slotNum(), packet.itemStack());

        PlayerInventoryHooks.MODIFY_CREATIVE_INVENTORY.invoker().onModify(event);
    }

    @Inject(
            method = "handleSetCreativeModeSlot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/InventoryMenu;broadcastChanges()V",
                    shift = At.Shift.AFTER
            )
    )
    public void kibu$onCreativeClickedSlot(ServerboundSetCreativeModeSlotPacket packet, CallbackInfo ci) {
        var event = new PlayerInventoryHooks.CreativeClickEvent(player, packet.slotNum(), packet.itemStack());

        PlayerInventoryHooks.MODIFIED_CREATIVE_INVENTORY.invoker().onModified(event);
    }

    @Inject(
            method = "handleMovePlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;getBoundingBox()Lnet/minecraft/world/phys/AABB;"
            ),
            cancellable = true
    )
    public void kibu$preMove(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        if (teleporting) return;

        if (Double.isNaN(lastX)) {
            lastX = player.getX();
            lastY = player.getY();
            lastZ = player.getZ();
            lastYaw = player.getYRot();
            lastPitch = player.getXRot();
        }

        double toX = clampHorizontal(packet.getX(lastX));
        double toY = clampVertical(packet.getY(lastY));
        double toZ = clampHorizontal(packet.getZ(lastZ));
        float toYaw = Mth.wrapDegrees(packet.getYRot(lastYaw));
        float toPitch = Mth.wrapDegrees(packet.getXRot(lastPitch));

        // check angle against last recorded position to debounce movement hook
        double distance = Math.pow(toX - lastX, 2) + Math.pow(toY - lastY, 2) + Math.pow(toZ - lastZ, 2);
        float angle = Math.abs(toYaw - lastYaw) + Math.abs(toPitch - lastPitch);

        if (distance < 0.00390625 && angle < 0.01f) return;

        PositionRotation from = new PositionRotation(lastX, lastY, lastZ, lastYaw, lastPitch);
        PositionRotation to = new PositionRotation(toX, toY, toZ, toYaw, toPitch);

        double motionY = player.getDeltaMovement().y();

        hookTeleported = false;

        boolean cancel = PlayerMoveCallback.HOOK.invoker().onMove(player, from, to);

        // the player was teleported by a hook
        if (hookTeleported) {
            ci.cancel();
            return;
        }

        if (cancel) {
            // movement disallowed; reset
            teleport(from.x(), from.y(), from.z(), from.getYaw(), from.getPitch());

            ci.cancel();
            return;
        }

        // in case a hook modified the y-velocity and the hook was not cancelled
        double newMotionY = player.getDeltaMovement().y();

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
            method = "handleMovePlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;jumpFromGround()V",
                    shift = At.Shift.AFTER
            )
    )
    public void kibu$onJump(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        if (Double.isNaN(modifiedVelocityY)) return;

        Vec3 velocity = player.getDeltaMovement();
        player.setDeltaMovement(velocity.x(), modifiedVelocityY, velocity.z());
    }

    @Inject(
            method = "handleMovePlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"
            )
    )
    public void kibu$resetModifiedVelocityY(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        modifiedVelocityY = Double.NaN;
    }

    @Inject(
            method = "teleport(Lnet/minecraft/world/entity/PositionMoveRotation;Ljava/util/Set;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;teleportSetPosition(Lnet/minecraft/world/entity/PositionMoveRotation;Ljava/util/Set;)V"
            )
    )
    public void kibu$onRequestTeleport(PositionMoveRotation pos, Set<Relative> flags, CallbackInfo ci) {
        hookTeleported = true;
        teleporting = true;
        lastX = pos.position().x;
        lastY = pos.position().y;
        lastZ = pos.position().z;
        lastYaw = pos.yRot();
        lastPitch = pos.xRot();
    }

    @Inject(
            method = "handlePlayerAbilities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/game/ServerboundPlayerAbilitiesPacket;isFlying()Z"
            ),
            cancellable = true
    )
    public void kibu$onToggleFlight(ServerboundPlayerAbilitiesPacket packet, CallbackInfo ci) {
        Abilities abilities = player.getAbilities();

        if (!abilities.mayfly) return;

        boolean fly = packet.isFlying();

        if (abilities.flying == fly) return;

        if (PlayerToggleFlightCallback.HOOK.invoker().onToggleFlight(player, fly)) {
            ci.cancel();
            player.onUpdateAbilities();
        }
    }

    @Inject(
            method = "handleAcceptTeleportPacket",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;awaitingPositionFromClient:Lnet/minecraft/world/phys/Vec3;",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            )
    )
    public void kibu$onTeleportConfirm(ServerboundAcceptTeleportationPacket packet, CallbackInfo ci) {
        teleporting = false;
        PlayerTeleportedCallback.HOOK.invoker().onTeleported(player);
    }

    @Inject(
            method = "handleAnimate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;resetLastActionTime()V"
            )
    )
    public void kibu$onHandSwing(ServerboundSwingPacket packet, CallbackInfo ci) {
        PlayerSwingHandHook.HOOK.invoker().onSwingHand(player, packet.getHand());
    }

    @Inject(
            method = "handlePlayerInput",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;setLastClientInput(Lnet/minecraft/world/entity/player/Input;)V"
            )
    )
    public void kibu$onPlayerInput(ServerboundPlayerInputPacket packet, CallbackInfo ci) {
        PlayerInputCallback.HOOK.invoker().onInput(player, packet.input());
    }
}
