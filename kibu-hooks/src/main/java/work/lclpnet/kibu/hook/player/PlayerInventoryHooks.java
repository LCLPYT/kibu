package work.lclpnet.kibu.hook.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public class PlayerInventoryHooks {

    private PlayerInventoryHooks() {}

    /**
     * Called when a player pressed one of the hot bar keys (0-9).
     */
    public static final Hook<SlotChange> SLOT_CHANGE = HookFactory.createArrayBacked(SlotChange.class, (hooks) -> (player, slot) -> {
        for (var hook : hooks) {
            hook.onChangeSlot(player, slot);
        }
    });

    /**
     * Called before the player drops an item.
     * Can be cancelled by returning true.
     */
    public static final Hook<DropItem> DROP_ITEM = HookFactory.createArrayBacked(DropItem.class, (hooks) -> (player, slot) -> {
        for (var hook : hooks) {
            if (hook.onDropItem(player, slot)) {
                return true;
            }
        }

        return false;
    });

    /**
     * Called after the player dropped an item.
     */
    public static final Hook<DroppedItem> DROPPED_ITEM = HookFactory.createArrayBacked(DroppedItem.class, (hooks) -> (player, slot) -> {
        for (var hook : hooks) {
            hook.onDroppedItem(player, slot);
        }
    });

    /**
     * Called before a player swap their selected main hand item with their offhand item.
     * Can be cancelled by returning true.
     */
    public static final Hook<SwapHands> SWAP_HANDS = HookFactory.createArrayBacked(SwapHands.class, (hooks) -> (player, slot) -> {
        for (var hook : hooks) {
            if (hook.onSwapHands(player, slot)) {
                return true;
            }
        }

        return false;
    });

    /**
     * Called after a player swapped their selected main hand item with their offhand item.
     */
    public static final Hook<SwappedHands> SWAPPED_HANDS = HookFactory.createArrayBacked(SwappedHands.class, (hooks) -> (player, slot) -> {
        for (var hook : hooks) {
            hook.onSwappedHands(player, slot);
        }
    });

    public interface SlotChange {

        void onChangeSlot(ServerPlayerEntity player, int slot);
    }

    public interface DropItem {

        boolean onDropItem(PlayerEntity player, int slot);
    }

    public interface DroppedItem {

        void onDroppedItem(PlayerEntity player, int slot);
    }

    public interface SwapHands {
        boolean onSwapHands(ServerPlayerEntity player, int slot);
    }

    public interface SwappedHands {
        void onSwappedHands(ServerPlayerEntity player, int slot);
    }
}
