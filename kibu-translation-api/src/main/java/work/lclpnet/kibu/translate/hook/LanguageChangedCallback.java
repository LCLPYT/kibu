package work.lclpnet.kibu.translate.hook;

import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface LanguageChangedCallback {

    Hook<LanguageChangedCallback> HOOK = HookFactory.createArrayBacked(LanguageChangedCallback.class, callbacks -> (player, language, reason) -> {
        for (var callback : callbacks) {
            callback.onChanged(player, language, reason);
        }
    });

    void onChanged(ServerPlayerEntity player, String language, Reason reason);

    enum Reason { PLAYER, OTHER }
}
