package work.lclpnet.kibu.translate.hook;

import net.minecraft.server.level.ServerPlayer;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface LanguageChangedCallback {

    Hook<LanguageChangedCallback> HOOK = HookFactory.createArrayBacked(LanguageChangedCallback.class, callbacks -> (player, language, reason) -> {
        for (var callback : callbacks) {
            callback.onChanged(player, language, reason);
        }
    });

    void onChanged(ServerPlayer player, String language, Reason reason);

    enum Reason { PLAYER, OTHER }
}
