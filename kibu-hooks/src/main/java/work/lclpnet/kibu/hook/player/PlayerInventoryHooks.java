package work.lclpnet.kibu.hook.player;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

import javax.annotation.Nullable;

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
    public static final Hook<DropItem> DROP_ITEM = HookFactory.createArrayBacked(DropItem.class, (hooks) -> (player, slot, inInventory) -> {
        boolean cancelled = false;

        for (var hook : hooks) {
            if (hook.onDropItem(player, slot, inInventory)) {
                cancelled = true;
            }
        }

        return cancelled;
    });

    /**
     * Called after the player dropped an item.
     */
    public static final Hook<DroppedItem> DROPPED_ITEM = HookFactory.createArrayBacked(DroppedItem.class, (hooks) -> (player, slot) -> {
        for (var hook : hooks) {
            hook.onDroppedItem(player, slot);
        }
    });

    public static final Hook<DropItemEntity> DROP_ITEM_ENTITY = HookFactory.createArrayBacked(DropItemEntity.class, (hooks) -> (player, itemEntity) -> {
        boolean cancel = false;

        for (var hook : hooks) {
            if (hook.onDropItemEntity(player, itemEntity)) {
                cancel = true;
            }
        }

        return cancel;
    });

    public static final Hook<DroppedItemEntity> DROPPED_ITEM_ENTITY = HookFactory.createArrayBacked(DroppedItemEntity.class, (hooks) -> (player, itemEntity) -> {
        for (var hook : hooks) {
            hook.onDroppedItemEntity(player, itemEntity);
        }
    });

    /**
     * Called before a player swap their selected main hand item with their offhand item.
     * Can be cancelled by returning true.
     */
    public static final Hook<SwapHands> SWAP_HANDS = HookFactory.createArrayBacked(SwapHands.class, (hooks) -> (player, slot) -> {
        boolean cancelled = false;

        for (var hook : hooks) {
            if (hook.onSwapHands(player, slot)) {
                cancelled = true;
            }
        }

        return cancelled;
    });

    /**
     * Called after a player swapped their selected main hand item with their offhand item.
     */
    public static final Hook<SwappedHands> SWAPPED_HANDS = HookFactory.createArrayBacked(SwappedHands.class, (hooks) -> (player, slot) -> {
        for (var hook : hooks) {
            hook.onSwappedHands(player, slot);
        }
    });

    /**
     * Called before an inventory click event is processed by the server.
     * Can be cancelled by returning true.
     */
    public static final Hook<InventoryModify> MODIFY_INVENTORY = HookFactory.createArrayBacked(InventoryModify.class, (hooks) -> (event) -> {
        boolean cancelled = false;

        for (var hook : hooks) {
            if (hook.onModify(event)) {
                cancelled = true;
            }
        }

        return cancelled;
    });

    /**
     * Called after an inventory click event was processed by the server.
     */
    public static final Hook<InventoryModified> MODIFIED_INVENTORY = HookFactory.createArrayBacked(InventoryModified.class, (hooks) -> (event) -> {
        for (var hook : hooks) {
            hook.onModified(event);
        }
    });

    /**
     * Called before a creative inventory click event is processed by the server.
     */
    public static final Hook<CreativeInventoryModify> MODIFY_CREATIVE_INVENTORY = HookFactory.createArrayBacked(CreativeInventoryModify.class, (hooks) -> (event) -> {
        for (var hook : hooks) {
            hook.onModify(event);
        }
    });

    /**
     * Called after a creative inventory click event was processed by the server.
     */
    public static final Hook<CreativeInventoryModified> MODIFIED_CREATIVE_INVENTORY = HookFactory.createArrayBacked(CreativeInventoryModified.class, (hooks) -> (event) -> {
        for (var hook : hooks) {
            hook.onModified(event);
        }
    });

    public static final Hook<ItemPickup> PLAYER_PICKUP = HookFactory.createArrayBacked(ItemPickup.class, (hooks) -> (player, itemEntity) -> {
        boolean cancelled = false;

        for (var hook : hooks) {
            if (hook.onPickup(player, itemEntity)) {
                cancelled = true;
            }
        }

        return cancelled;
    });

    public static final Hook<ItemPickedUp> PLAYER_PICKED_UP = HookFactory.createArrayBacked(ItemPickedUp.class, (hooks) -> (player, itemEntity) -> {
        for (var hook : hooks) {
            hook.onPickedUp(player, itemEntity);
        }
    });

    public interface SlotChange {

        void onChangeSlot(ServerPlayerEntity player, int slot);
    }

    public interface DropItem {

        boolean onDropItem(PlayerEntity player, int slot, boolean inInventory);
    }

    public interface DroppedItem {

        void onDroppedItem(PlayerEntity player, int slot);
    }

    public interface DropItemEntity {
        boolean onDropItemEntity(ServerPlayerEntity player, ItemEntity itemEntity);
    }

    public interface DroppedItemEntity {
        void onDroppedItemEntity(ServerPlayerEntity player, ItemEntity itemEntity);
    }

    public interface SwapHands {
        boolean onSwapHands(ServerPlayerEntity player, int slot);
    }

    public interface SwappedHands {
        void onSwappedHands(ServerPlayerEntity player, int slot);
    }

    public interface InventoryModify {
        boolean onModify(ClickEvent event);
    }

    public interface InventoryModified {
        void onModified(ClickEvent event);
    }

    public interface CreativeInventoryModify {
        void onModify(CreativeClickEvent event);
    }

    public interface CreativeInventoryModified {
        void onModified(CreativeClickEvent event);
    }

    public record ClickEvent(ServerPlayerEntity player, int slot, int button, ItemStack cursorStack,
                             SlotActionType action, Int2ObjectMap<ItemStack> modified) {
        public boolean isDropAction() {
            return action == SlotActionType.THROW || (action == SlotActionType.PICKUP && slot == -999);
        }

        @Nullable
        public Slot handlerSlot() {
            if (player.currentScreenHandler == null || slot == -1 || slot == -999 || slot >= player.currentScreenHandler.slots.size()) {
                return null;
            }

            return player.currentScreenHandler.getSlot(slot);
        }

        @Nullable
        public ItemStack clickedStack() {
            Slot slot = handlerSlot();

            if (slot == null) return null;

            return slot.getStack();
        }

        @Nullable
        public Inventory inventory() {
            PlayerInventory inv = player.getInventory();

            if (player.currentScreenHandler == null) {
                return inv;
            }

            Slot slot = handlerSlot();
            if (slot == null) {
                return null;
            }

            return slot.inventory;
        }

        @Nullable
        public Inventory targetInventory() {
            if (action != SlotActionType.QUICK_MOVE) return null;

            Inventory src = inventory();
            if (src == null) return null;

            boolean srcChange = false;

            for (int i : modified().keySet()) {
                Slot slot = player.currentScreenHandler.getSlot(i);
                if (slot == null) continue;

                if (!src.equals(slot.inventory)) {
                    return slot.inventory;
                }

                // skip the source inventory once
                if (!srcChange) {
                    srcChange = true;
                    continue;
                }

                return slot.inventory;
            }

            return null;
        }

        @Override
        public String toString() {
            return "ClickEvent{player=%s, slot=%d, button=%d, cursorStack=%s, action=%s, modified=%s}"
                    .formatted(player, slot, button, cursorStack, action, modified);
        }
    }

    public record CreativeClickEvent(ServerPlayerEntity player, int slot, ItemStack stack) {

        @Override
        public String toString() {
            return "CreativeClickEvent{player=%s, slot=%d, stack=%s}"
                    .formatted(player, slot, stack);
        }
    }

    public interface ItemPickup {
        boolean onPickup(PlayerEntity player, ItemEntity itemEntity);
    }

    public interface ItemPickedUp {
        void onPickedUp(PlayerEntity player, ItemEntity itemEntity);
    }
}
