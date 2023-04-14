package work.lclpnet.kibu.hook.player;

import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public class PlayerInventoryHooks {

    private PlayerInventoryHooks() {}

    public static final Hook<SlotChange> SLOT_CHANGE = HookFactory.createArrayBacked(SlotChange.class, (hooks) -> (slot) -> {
        for (var hook : hooks) {
            hook.onChangeSlot(slot);
        }
    });

    public interface SlotChange {

        void onChangeSlot(int slot);
    }
}
