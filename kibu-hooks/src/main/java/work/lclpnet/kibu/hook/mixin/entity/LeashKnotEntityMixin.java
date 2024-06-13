package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.LeashDetachCallback;
import work.lclpnet.kibu.hook.entity.LeashEntityToBlockCallback;

@Mixin(LeashKnotEntity.class)
public class LeashKnotEntityMixin {

    @WrapWithCondition(
            method = "interact",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Leashable;attachLeash(Lnet/minecraft/entity/Entity;Z)V"
            )
    )
    public boolean kibu$attachLeashAllowed(Leashable instance, Entity leashHolder, boolean sendPacket) {
        Entity currentHolder = instance.getLeashHolder();
        if (!(currentHolder instanceof PlayerEntity player) || !(leashHolder instanceof LeashKnotEntity leashKnot)) return true;

        return !LeashEntityToBlockCallback.HOOK.invoker().onLeashToBlock(player, instance, leashKnot);
    }

    @Inject(
            method = "interact",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/decoration/LeashKnotEntity;discard()V"
            ),
            cancellable = true
    )
    public void kibu$onDetachLeash(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        LeashKnotEntity leashKnot = (LeashKnotEntity) (Object) this;

        if (LeashDetachCallback.HOOK.invoker().onDetach(player, leashKnot)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
