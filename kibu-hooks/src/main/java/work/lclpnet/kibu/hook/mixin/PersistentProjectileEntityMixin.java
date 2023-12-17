package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.ProjectilePickupCallback;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {

    @Inject(
            method = "tryPickup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"
            ),
            cancellable = true
    )
    public void kibu$onPickup(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        PersistentProjectileEntity self = (PersistentProjectileEntity) (Object) this;

        if (ProjectilePickupCallback.HOOK.invoker().onPickup(player, self)) {
            cir.setReturnValue(false);
        }
    }
}
