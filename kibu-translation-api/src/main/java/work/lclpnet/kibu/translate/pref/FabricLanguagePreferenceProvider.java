package work.lclpnet.kibu.translate.pref;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class FabricLanguagePreferenceProvider implements LanguagePreferenceProvider {

    private final LanguagePreferenceProvider[] providers;

    private FabricLanguagePreferenceProvider() {
        FabricLoader instance = FabricLoader.getInstance();

        // find language preference provider mods
        providers = instance.getEntrypoints("kibu-language", LanguagePreferenceProvider.class)
                .toArray(LanguagePreferenceProvider[]::new);
    }

    @Override
    public Optional<String> getLanguagePreference(ServerPlayerEntity player) {
        for (LanguagePreferenceProvider provider : providers) {
            var preference = provider.getLanguagePreference(player);

            if (preference.isPresent()) {
                return preference;
            }
        }

        return Optional.empty();
    }

    public static LanguagePreferenceProvider getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final FabricLanguagePreferenceProvider instance = new FabricLanguagePreferenceProvider();
    }
}
