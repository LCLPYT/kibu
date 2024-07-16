package work.lclpnet.kibu.hook.world;

import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface WorldUnreadyCallback {

    Hook<WorldUnreadyCallback> HOOK = HookFactory.createArrayBacked(WorldUnreadyCallback.class, callbacks -> () -> {
        for (WorldUnreadyCallback callback : callbacks) {
            callback.onWorldUnready();
        }
    });

    void onWorldUnready();
}
