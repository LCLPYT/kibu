package work.lclpnet.kibu.translate.mixin;

import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.translate.hook.LanguageChangedCallback;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(
            method = "updateOptions",
            at = @At("HEAD")
    )
    public void kibu$fireLanguageEvent(ClientInformation information, CallbackInfo ci) {
        ServerPlayer self = (ServerPlayer) (Object) this;

        // ignore early call during the join process
        if (self.connection == null) return;

        LanguageChangedCallback.HOOK.invoker().onChanged(self, information.language(), LanguageChangedCallback.Reason.PLAYER);
    }
}
