package work.lclpnet.kibu.hook.player;

import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface PlayerSlotChangeHook {

    Hook<PlayerSlotChangeHook> HOOK = HookFactory.createArrayBacked(PlayerSlotChangeHook.class, (hooks) -> (slot) -> {
        for (var hook : hooks) {
            hook.onChangeSlot(slot);
        }
    });

    void onChangeSlot(int slot);
}
