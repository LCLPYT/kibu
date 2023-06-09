package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.type.PlayerAware;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Shadow protected HungerManager hungerManager;

    @Inject(
            method = "<init>*",
            at = @At("RETURN")
    )
    public void kibu$onInit(CallbackInfo ci) {
        //noinspection DataFlowIssue
        ((PlayerAware) hungerManager).kibu$setPlayer((PlayerEntity) (Object) this);
    }
}
