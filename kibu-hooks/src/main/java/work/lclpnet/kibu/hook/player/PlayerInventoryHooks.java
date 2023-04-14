package work.lclpnet.kibu.hook.player;

import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public class PlayerInventoryHooks {

    private PlayerInventoryHooks() {}

    public static final Hook<SlotChange> SLOT_CHANGE = HookFactory.createArrayBacked(SlotChange.class, (hooks) -> (player, slot) -> {
        for (var hook : hooks) {
            hook.onChangeSlot(player, slot);
        }
    });

    public interface SlotChange {

        void onChangeSlot(ServerPlayerEntity player, int slot);
    }
}
