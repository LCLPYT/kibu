package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.LeashEntityCallback;
import work.lclpnet.kibu.hook.entity.UnleashEntityCallback;

@Mixin(MobEntity.class)
public class MobEntityMixin {

    @Inject(
            method = "interactWithItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;attachLeash(Lnet/minecraft/entity/Entity;Z)V"
            ),
            cancellable = true
    )
    public void kibu$beforeLeashMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        MobEntity self = (MobEntity) (Object) this;

        if (LeashEntityCallback.HOOK.invoker().onLeash(player, self)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }

    @Inject(
            method = "interact",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;detachLeash(ZZ)V"
            ),
            cancellable = true
    )
    public void kibu$beforeUnleashMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        MobEntity self = (MobEntity) (Object) this;

        if (UnleashEntityCallback.HOOK.invoker().onUnleash(player, self)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
