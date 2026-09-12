package work.lclpnet.kibu.hook.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface ShelfPlaceItemCallback {

    Hook<ShelfPlaceItemCallback> HOOK = HookFactory.createArrayBacked(ShelfPlaceItemCallback.class,
            callbacks -> (world, pos, slot, stack, player) -> {
                boolean cancel = false;

                for (var cb : callbacks) {
                    if (cb.onPlaceItem(world, pos, slot, stack, player)) {
                        cancel = true;
                    }
                }

                return cancel;
            });

    /**
     * Called before a player puts an item into a shelf slot.
     *
     * @param world The world the shelf is in.
     * @param pos The position of the shelf block. For connected shelves, this is the part that owns the slot.
     * @param slot The shelf slot the item would be put into.
     * @param stack The stack that would be put into the slot.
     * @param player The interacting player.
     * @return Whether the interaction should be cancelled.
     */
    boolean onPlaceItem(Level world, BlockPos pos, int slot, ItemStack stack, Player player);
}
