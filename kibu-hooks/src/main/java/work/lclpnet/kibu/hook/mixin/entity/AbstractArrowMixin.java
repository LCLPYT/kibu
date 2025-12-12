package work.lclpnet.kibu.hook.mixin.entity;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.ProjectilePickupCallback;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {

    @Inject(
            method = "tryPickup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z"
            ),
            cancellable = true
    )
    public void kibu$onPickup(Player player, CallbackInfoReturnable<Boolean> cir) {
        AbstractArrow self = (AbstractArrow) (Object) this;

        if (ProjectilePickupCallback.HOOK.invoker().onPickup(player, self)) {
            cir.setReturnValue(false);
        }
    }
}
