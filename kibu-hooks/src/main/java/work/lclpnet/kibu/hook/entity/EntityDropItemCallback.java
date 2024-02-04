package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.world.World;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface EntityDropItemCallback {

    Hook<EntityDropItemCallback> HOOK = HookFactory.createArrayBacked(EntityDropItemCallback.class, callbacks -> (world, entity, itemEntity) -> {
        boolean cancel = false;

        for (var callback : callbacks) {
            if (callback.onDropItem(world, entity, itemEntity)) {
                cancel = true;
            }
        }

        return cancel;
    });

    boolean onDropItem(World world, Entity entity, ItemEntity itemEntity);
}
