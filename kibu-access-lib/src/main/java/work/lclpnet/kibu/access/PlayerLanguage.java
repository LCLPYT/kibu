package work.lclpnet.kibu.access;

import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.access.type.LanguageGetter;

import javax.annotation.Nonnull;

public class PlayerLanguage {

    private PlayerLanguage() {}

    /**
     * Gets the currently selected language of a player.
     * @param player The player.
     * @return The language code, such as "en_us".
     * @implNote In case this method is invoked before a client sends the
     * {@link net.minecraft.network.packet.c2s.common.SyncedClientOptions}, the language defaults to "en_us".
     */
    @Nonnull
    public static String getLanguage(ServerPlayerEntity player) {
        return ((LanguageGetter) player).kibu$getLanguage();
    }
}
