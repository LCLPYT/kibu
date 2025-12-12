package work.lclpnet.kibu.translate.pref;

import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public interface LanguagePreferenceProvider {

    Optional<String> getLanguagePreference(ServerPlayer player);
}
