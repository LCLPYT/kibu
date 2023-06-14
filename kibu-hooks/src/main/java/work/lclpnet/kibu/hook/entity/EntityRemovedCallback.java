package work.lclpnet.kibu.hook.entity;

import net.minecraft.entity.Entity;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public interface EntityRemovedCallback {

    Hook<EntityRemovedCallback> HOOK = HookFactory.createArrayBacked(EntityRemovedCallback.class, hooks -> (entity, removalReason) -> {
        for (var hook : hooks) {
            hook.onRemove(entity, removalReason);
        }
    });

    void onRemove(Entity entity, Entity.RemovalReason removalReason);
}
