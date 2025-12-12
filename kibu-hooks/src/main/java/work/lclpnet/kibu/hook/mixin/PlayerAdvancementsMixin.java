package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.player.PlayerAdvancementPacketCallback;

@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsMixin {

    @WrapWithCondition(
            method = "flushDirty",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"
            )
    )
    public boolean kibu$onSendUpdate(ServerGamePacketListenerImpl instance, Packet<?> packet) {
        if (!(packet instanceof ClientboundUpdateAdvancementsPacket advancementPacket)) return true;

        return !PlayerAdvancementPacketCallback.HOOK.invoker().onAdvancementUpdate(instance.player, advancementPacket);
    }
}
