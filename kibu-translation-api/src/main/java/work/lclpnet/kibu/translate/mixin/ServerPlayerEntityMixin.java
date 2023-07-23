package work.lclpnet.kibu.translate.mixin;

import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.translate.hook.LanguageChangedCallback;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(
            method = "setClientSettings",
            at = @At("HEAD")
    )
    public void kibu$fireLanguageEvent(ClientSettingsC2SPacket packet, CallbackInfo ci) {
        @SuppressWarnings("DataFlowIssue")
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;

        LanguageChangedCallback.HOOK.invoker().onChanged(self, packet.language(), LanguageChangedCallback.Reason.PLAYER);
    }
}
