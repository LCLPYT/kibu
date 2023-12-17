package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.AdvancementUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.player.PlayerAdvancementPacketCallback;

@Mixin(PlayerAdvancementTracker.class)
public abstract class PlayerAdvancementHandlerMixin {

    @WrapWithCondition(
            method = "sendUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V"
            )
    )
    public boolean kibu$onSendUpdate(ServerPlayNetworkHandler instance, Packet<?> packet) {
        if (!(packet instanceof AdvancementUpdateS2CPacket advancementPacket)) return true;

        return !PlayerAdvancementPacketCallback.HOOK.invoker().onAdvancementUpdate(instance.player, advancementPacket);
    }
}
