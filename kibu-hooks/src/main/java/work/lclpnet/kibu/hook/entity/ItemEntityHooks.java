package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public class ItemEntityHooks {

    private ItemEntityHooks() {}

    public static final Hook<ItemPickup> PLAYER_PICKUP = HookFactory.createArrayBacked(ItemPickup.class, (hooks) -> (player, itemEntity) -> {
        for (var hook : hooks) {
            if (hook.onPickup(player, itemEntity)) {
                return true;
            }
        }

        return false;
    });

    public static final Hook<ItemPickedUp> PLAYER_PICKED_UP = HookFactory.createArrayBacked(ItemPickedUp.class, (hooks) -> (player, itemEntity) -> {
        for (var hook : hooks) {
            hook.onPickedUp(player, itemEntity);
        }
    });

    public interface ItemPickup {
        boolean onPickup(PlayerEntity player, ItemEntity itemEntity);
    }

    public interface ItemPickedUp {
        void onPickedUp(PlayerEntity player, ItemEntity itemEntity);
    }
}
