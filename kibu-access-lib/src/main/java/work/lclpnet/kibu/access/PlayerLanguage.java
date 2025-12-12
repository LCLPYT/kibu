package work.lclpnet.kibu.access;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import work.lclpnet.kibu.access.type.LanguageGetter;

public class PlayerLanguage {

    private PlayerLanguage() {}

    /**
     * Gets the currently selected language of a player.
     * @param player The player.
     * @return The language code, such as "en_us".
     * @implNote In case this method is invoked before a client sends the
     * {@link net.minecraft.server.level.ClientInformation}, the language defaults to "en_us".
     */
    @NotNull
    public static String getLanguage(ServerPlayer player) {
        return ((LanguageGetter) player).kibu$getLanguage();
    }
}
