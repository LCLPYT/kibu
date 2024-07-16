package work.lclpnet.kibu.hook.world;

import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface WorldReadyCallback {

    Hook<WorldReadyCallback> HOOK = HookFactory.createArrayBacked(WorldReadyCallback.class, callbacks -> () -> {
        for (WorldReadyCallback callback : callbacks) {
            callback.onWorldReady();
        }
    });

    void onWorldReady();
}
