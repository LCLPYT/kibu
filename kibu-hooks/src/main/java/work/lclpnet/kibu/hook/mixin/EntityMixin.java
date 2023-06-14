package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.EntityRemovedCallback;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(
            method = "setRemoved",
            at = @At("TAIL")
    )
    public void kibu$onRemoved(Entity.RemovalReason reason, CallbackInfo ci) {
        @SuppressWarnings("DataFlowIssue")
        Entity self = (Entity) (Object) this;

        EntityRemovedCallback.HOOK.invoker().onRemove(self, reason);
    }
}
