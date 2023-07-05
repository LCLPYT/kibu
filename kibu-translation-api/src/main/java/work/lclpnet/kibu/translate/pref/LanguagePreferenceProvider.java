package work.lclpnet.kibu.translate.pref;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public interface LanguagePreferenceProvider {

    Optional<String> getLanguagePreference(ServerPlayerEntity player);
}
