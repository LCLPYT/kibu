package work.lclpnet.kibu.access.mixin;

import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.access.type.LanguageGetter;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements LanguageGetter {

    @Unique
    private String language = "en_us";  // default language

    @Inject(
            method = "setClientSettings",
            at = @At("HEAD")
    )
    public void kibu$updateClientSettings(ClientSettingsC2SPacket packet, CallbackInfo ci) {
        language = packet.language();
    }

    @NotNull
    @Override
    public String kibu$getLanguage() {
        return language;
    }
}
