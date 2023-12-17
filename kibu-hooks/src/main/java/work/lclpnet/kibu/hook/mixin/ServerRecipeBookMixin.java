package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.UnlockRecipesS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerRecipeBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.player.PlayerRecipePacketCallback;

@Mixin(ServerRecipeBook.class)
public class ServerRecipeBookMixin {

    @WrapWithCondition(
            method = "sendUnlockRecipesPacket",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V"
            )
    )
    public boolean kibu$beforeSendUnlockPacket(ServerPlayNetworkHandler instance, Packet<?> packet) {
        if (!(packet instanceof UnlockRecipesS2CPacket updatePacket)) return true;

        ServerPlayerEntity player = instance.player;

        boolean cancel = PlayerRecipePacketCallback.HOOK.invoker().onRecipeUpdate(player, updatePacket);
        if (!cancel) return true;

        player.getRecipeBook().sendInitRecipesPacket(player);
        return false;
    }
}
