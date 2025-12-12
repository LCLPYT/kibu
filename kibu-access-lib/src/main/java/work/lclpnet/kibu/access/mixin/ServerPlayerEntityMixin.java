package work.lclpnet.kibu.access.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import work.lclpnet.kibu.access.type.LanguageGetter;

@Mixin(ServerPlayer.class)
public class ServerPlayerEntityMixin implements LanguageGetter {

    @Shadow private String language;

    @NotNull
    @Override
    public String kibu$getLanguage() {
        return language;
    }
}
