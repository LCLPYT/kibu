package work.lclpnet.kibu.translate.mixin;

import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.translate.hook.LanguageChangedCallback;

@Mixin(ServerPlayer.class)
public class ServerPlayerEntityMixin {

    @Inject(
            method = "updateOptions",
            at = @At("HEAD")
    )
    public void kibu$fireLanguageEvent(ClientInformation packet, CallbackInfo ci) {
        ServerPlayer self = (ServerPlayer) (Object) this;

        // ignore early call during the join process
        if (self.connection == null) return;

        LanguageChangedCallback.HOOK.invoker().onChanged(self, packet.language(), LanguageChangedCallback.Reason.PLAYER);
    }
}
