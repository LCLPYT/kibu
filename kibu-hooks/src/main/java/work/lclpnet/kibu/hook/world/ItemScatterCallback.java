package work.lclpnet.kibu.hook.world;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface ItemScatterCallback {

    Hook<ItemScatterCallback> HOOK = HookFactory.createArrayBacked(ItemScatterCallback.class, callbacks -> (world, x, y, z, stack) -> {
        boolean cancel = false;

        for (var callback : callbacks) {
            if (callback.onScatter(world, x, y, z, stack)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onScatter(World world, double x, double y, double z, ItemStack stack);
}
